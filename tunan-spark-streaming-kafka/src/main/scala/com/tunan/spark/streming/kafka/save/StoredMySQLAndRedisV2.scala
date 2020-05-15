package com.tunan.spark.streming.kafka.save

import com.tunan.spark.streming.kafka.offmanager.RedisOffsetsManager
import com.tunan.spark.utils.mysql.MySQLUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

object StoredMySQLAndRedisV2 {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val ssc = new StreamingContext(conf, Seconds(5))

        val GROUPED = "use_a_separate_group_id_for_each_stream_2"

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
        val fromOffsets = RedisOffsetsManager.obtainOffsets(topics, GROUPED)

        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams, fromOffsets)
        )


        stream.foreachRDD(rdd => {
            if (!rdd.isEmpty()) {
                // 拿到offsetRanges 包括topic、partition、fromOffset、untilOffset
                val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
                rdd.map(x => (x.value(), 1)).reduceByKey(_ + _)
                  .foreachPartition(partition => {
                      MySQLUtils.connect()

                      val buffer = new ListBuffer[Seq[(Symbol, Any)]]()
                      // Seq(Seq('id -> 1, 'name -> "Alice"), Seq('id -> 2, 'name -> "Bob"))
                      partition.foreach(x => {
                          buffer.append(Seq('word -> x._1, 'cnt -> x._2))
                      })
                      try {
                          // TODO 批次插入
                          MySQLUtils.insertByBatch(s"replace into wc(word,cnt) values({word},{cnt})", buffer)
                          // TODO 手动异常
//                          4 / 0
                          // TODO 写offset
                          RedisOffsetsManager.storeOffsets(offsetRanges, GROUPED)
                      } catch {
                          case e: Exception => e.printStackTrace()
                      } finally {
                          MySQLUtils.close()
                      }
                  })
            }
        })
        ssc.start()
        ssc.awaitTermination()
    }
}
