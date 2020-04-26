package com.tunan.spark.streming.kafka.offmanager

import java.util

import com.tunan.spark.utils.redis.RedisUtils
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import redis.clients.jedis.Jedis

object RedisOffsetsManager extends OffsetsManager {

    // TODO 拿到offset
    override def obtainOffsets(topics: Array[String], groupId: String): Map[TopicPartition, Long] = {
        // 定义fromOffsets、jedis、offsetMap
        var fromOffsets: Map[TopicPartition, Long] = Map[TopicPartition, Long]()
        var jedis: Jedis = null
        try {
            // 初始化jedis
            jedis = RedisUtils.getJedis
            // 拿到offsetMap
            val offsetMap = jedis.hgetAll(topics(0) + "_" + groupId)
            // 导入java集合转scala的依赖
//            import scala.collection.JavaConverters._
            import scala.collection.JavaConversions._
            offsetMap.foreach(row => {
                // 拿到topicPartition
                val topicPartition = new TopicPartition(topics(0), row._1.toInt)
                // 将topicPartition和offset放入fromOffsets
                fromOffsets += topicPartition -> row._2.toLong
            })
        } catch {
            case e:Exception => e.printStackTrace()
        } finally {
            // 关闭jedis
            jedis.close()
        }
        // 返回fromOffsets
        fromOffsets
    }
    // TODO 存储offset
    override def storeOffsets(offsetRanges: Array[OffsetRange], groupId: String): Unit = {
        // 定义jedis
        var jedis:Jedis = null
        try {
            // 初始化jedis
            jedis = RedisUtils.getJedis
            offsetRanges.foreach(o  => {
                // 存储offset
                jedis.hset(o.topic + "_" + groupId, o.partition + "", o.untilOffset + "")
            })
        } catch {
            case e:Exception => e.printStackTrace()
        } finally {
            // 关闭jedis0

            jedis.close()
        }
    }
}
