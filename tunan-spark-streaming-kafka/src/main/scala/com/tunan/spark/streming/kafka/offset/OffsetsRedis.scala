package com.tunan.spark.streming.kafka.offset

import com.tunan.spark.streming.kafka.offmanager.RedisOffsetsManager
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

object OffsetsRedis {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val ssc = new StreamingContext(conf, Seconds(5))

        // TODO 消费者组
        val groupId = "view_group"
        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "hadoop:9090,hadoop:9091,hadoop:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> groupId,
            "auto.offset.reset" -> "earliest", //latest
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )

        // TODO 拿到topic
        val topics = Array("view_topic") // topic

        // TODO 拿到偏移量
        val fromOffsets = RedisOffsetsManager.obtainOffsets(topics,groupId) //Map[TopicPartition, Long]()

        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams, fromOffsets)
        )

        stream.foreachRDD(rdd => {
            if (!rdd.isEmpty()) {
                // 拿到offsetRanges 包括topic、partition、fromOffset、untilOffset
                val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

                // 已经拿到了offset，可以开始业务逻辑处理
                val result = rdd.map(row => {
                    (row.value(), 1)
                }).reduceByKey(_ + _)
                  .collect()

                // 事物写入redis
                var jedis: Jedis = null
                try {
//                    jedis = RedisUtils.getJedis

                    // 提交业务逻辑
                    for (pair <- result) {
                        jedis.hincrBy("wc_redis_ss", pair._1, pair._2)
                        println(pair._1+" "+pair._2)
                    }


                    // 写offset
                    RedisOffsetsManager.storeOffsets(offsetRanges,groupId)

                } catch {
                    case e: Exception =>
                        e.printStackTrace()
                } finally {
                    jedis.close()
                }
            } else {
                println(s"拉取数据中...")
            }
        })
        // 开始作业
        ssc.start()
        ssc.awaitTermination()
    }
}
