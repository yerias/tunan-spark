package com.tunan.spark.streming.kafka.offmanager

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange

object MySQLOffsetsManager extends OffsetsManager {
    override def obtainOffsets(topics: Array[String], groupId: String): Map[TopicPartition, Long] = ???

    override def storeOffsets(offsetRanges: Array[OffsetRange], groupId: String): Unit = ???
}
