package com.tunan.spark.streming.kafka.offmanager

import com.tunan.spark.streming.kafka.utils.HBaseUtils
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange

import scala.collection.JavaConversions

object HBaseOffsetsManager extends OffsetsManager {
    override def obtainOffsets(topics: Array[String], group: String): Map[TopicPartition, Long] = {
        //1. 定义一个结构专门保存偏移量
        var offsets: Map[TopicPartition, Long] = Map[TopicPartition, Long]()
        //1.1 获取到HBase connection
        val connection = HBaseUtils.getConnection
        val tableName = TableName.valueOf("tunan:spark-topic-offset")
        val cf = Bytes.toBytes("cf")

        //2. 遍历主题,只有一个topic
        //2.1 自定义rowkey
        val rk = s"${topics(0)}-${group}".getBytes()
        //2.2 获取表的分区以及对应的偏移量
        val partition2Offsets = HBaseUtils.getColValue(connection, tableName, rk, cf)
        val partition2Offsets2 = JavaConversions.mapAsScalaMap(partition2Offsets)
        //2.3 遍历获取分区:还需要将java的数组转换位scala的数组
        for ((k, v) <- partition2Offsets2) {
            // 拿到topicPartition
            val topicPartition = new TopicPartition(topics(0), (k + "").toInt)
            // 将topicPartition和offset放入fromOffsets
            offsets += topicPartition -> v
        }
        HBaseUtils.release(connection)
        offsets
    }

    override def storeOffsets(offsetRanges: Array[OffsetRange], group: String): Unit = {
        //0.
        val connection = HBaseUtils.getConnection
        val tableName = TableName.valueOf("tunan:spark-topic-offset")
        val cf = Bytes.toBytes("cf")
        //1. 遍历偏移量范围的数组
        for (offsetRange <- offsetRanges) {
            //2. 获取主题分区以及偏移量
            val rk = s"${offsetRange.topic}-${group}".getBytes()
            val partition = offsetRange.partition
            val untilOffset = offsetRange.untilOffset

            //3. 将结果保存到hbase
            HBaseUtils.set(connection, tableName, rk, cf, (partition + "").getBytes(), (untilOffset + "").getBytes())

            HBaseUtils.release(connection)
        }
    }
}
