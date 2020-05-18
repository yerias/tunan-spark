package com.tunan.spark.streming.kafka.save

import java.util.concurrent.locks.ReentrantLock

import com.tunan.spark.streming.kafka.offmanager.RedisOffsetsManager
import com.tunan.spark.utils.mysql.MySQLUtils
import com.tunan.spark.utils.redis.RedisUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import redis.clients.jedis.{Jedis, Pipeline}

object StoredMySQLAndRedis {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val ssc = new StreamingContext(conf, Seconds(5))


        val GROUPED = "use_a_separate_group_id_for_each_stream_1"

        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "hadoop:9090,hadoop:9091,hadoop:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> GROUPED,
            "auto.offset.reset" -> "earliest", //latest 、 earliest
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )

        val topics = Array("test")
        // TODO 拿到偏移量
        val fromOffsets = RedisOffsetsManager.obtainOffsets(topics,GROUPED)

        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams,fromOffsets)
        )

        val lock = new ReentrantLock()

        stream
          .foreachRDD(rdd =>{
              if (!rdd.isEmpty()) {
                  // 拿到offsetRanges 包括topic、partition、fromOffset、untilOffset
                  val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

                  rdd.map(x => (x.value(),1)).reduceByKey(_+_)
                    .foreachPartition(partition =>{
//                        var pipeline: Pipeline = null
//                        var jedis: Jedis = null
                        MySQLUtils.connect()
                      try {
                          // 事物写入redis
//                          jedis = RedisUtils.getJedis
//                          pipeline = jedis.pipelined()
//                          pipeline.multi()

                          // 没保证MySQL的事物
                          partition.foreach(row => {
                                  // 提交业务逻辑
                                MySQLUtils.insert(s"insert into wc(word,cnt) values('${row._1}',${row._2})")
                          })

                          // TODO 写offset
/*                          offsetRanges.foreach(o => {
                              pipeline.hset(topics(0) + "_" + GROUPED, o.partition + "", o.untilOffset + "")
                          })*/
                          RedisOffsetsManager.storeOffsets(offsetRanges,GROUPED)


                          // 提交&同步
//                          pipeline.exec()
//                          pipeline.sync()
                      } catch {
                          case  e:Exception=>
//                              pipeline.discard()
                              e.printStackTrace()
                      } finally {
                        // 关闭连接
//                        pipeline.close()
//                        jedis.close()
                        MySQLUtils.close()
                      }
                  })
              }
          })
        ssc.start()
        ssc.awaitTermination()
    }
}
