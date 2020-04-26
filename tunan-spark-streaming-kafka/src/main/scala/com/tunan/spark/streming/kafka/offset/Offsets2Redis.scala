package com.tunan.spark.streming.kafka.offset

import java.util

import com.tunan.spark.streming.kafka.offmanager.RedisOffsetsManager
import com.tunan.spark.utils.redis.RedisUtils
import com.typesafe.config.ConfigFactory
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.{Jedis, Pipeline}

object Offsets2Redis {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val ssc = new StreamingContext(conf, Seconds(5))

        val kafkaConf = ConfigFactory.load("kafka.conf")
        val GROUPED = kafkaConf.getString("kafka.groupId")
        val BOOTSTRAPS = kafkaConf.getString("kafka.bootstrap.servers")
        val AUSTEREST = kafkaConf.getString("kafka.auto.offset.reset")

        // TODO 消费者组

        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> BOOTSTRAPS,
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> GROUPED,
            "auto.offset.reset" -> AUSTEREST, //latest
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )

        // TODO 拿到topic
        val topics = Array("test") // topic

        // TODO 拿到偏移量
        val fromOffsets = RedisOffsetsManager.obtainOffsets(topics,GROUPED) //Map[TopicPartition, Long]()

        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams, fromOffsets)
        )

        stream.foreachRDD(rdd => {
            if (!rdd.isEmpty()) {
                // 拿到offsetRanges 包括topic、partition、fromOffset、untilOffset
                val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
                offsetRanges.foreach { o =>
                    println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
                }

                // 已经拿到了offset，可以开始业务逻辑处理
                val result = rdd.map(row => {
                    (row.value(), 1)
                }).reduceByKey(_ + _)
                  .collect()

                // 事物写入redis
                var pipeline: Pipeline = null
                var jedis: Jedis = null
                try {
                    jedis = RedisUtils.getJedis
                    pipeline = jedis.pipelined()
                    pipeline.multi()

                    // 提交业务逻辑
                    for (pair <- result) {
                        pipeline.hincrBy("wc_redis_ss", pair._1, pair._2)
                    }

                    // 写offset
                    offsetRanges.foreach(o => {
                        pipeline.hset(topics(0) + "_" + GROUPED, o.partition + "", o.untilOffset + "")
                    })

                    // 提交&同步
                    pipeline.exec()
                    pipeline.sync()

                } catch {
                    case e: Exception =>
                        // 失败回滚
                        pipeline.discard()
                        e.printStackTrace()
                } finally {
                    // 关闭连接
                    pipeline.close()
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
