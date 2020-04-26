package com.tunan.spark.streming.kafka.offmanager

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange

trait OffsetsManager {

    def obtainOffsets(topics: Array[String], groupId: String): Map[TopicPartition, Long]

    def storeOffsets(offsetRanges: Array[OffsetRange], groupId: String)
}
