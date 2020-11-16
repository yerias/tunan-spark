package com.tunan.spark.streming.kafka.offmanager

import java.sql.{DriverManager, ResultSet}

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange


/*
    TODO 没测试 mysql的连接要做成连接池
 */
object MySQLOffsetsManager extends OffsetsManager {
    override def obtainOffsets(topics: Array[String], groupId: String): Map[TopicPartition, Long] = {
        var offsets: Map[TopicPartition, Long] = Map[TopicPartition, Long]()

        val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata?characterEncoding=utf-8", "root", "123456")

        val pstm = conn.prepareStatement("select * from mysqloffset where groupId = ? and topic = ? ")
        pstm.setString(1, groupId)
        pstm.setString(2, topics(0))

        val result: ResultSet = pstm.executeQuery()
        while (result.next()) {
            // 把数据库中的偏移量数据加载了
            val p = result.getInt("partition")
            val f = result.getInt("untilOffset")
            //      offsets += (new TopicPartition(topic,p)-> f)
            val partition: TopicPartition = new TopicPartition(topics(0), p)
            offsets += partition -> f
        }
        offsets
    }

    override def storeOffsets(offsetRanges: Array[OffsetRange], groupId: String): Unit = {
        val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata?characterEncoding=utf-8", "root", "123456")

        for (offsetRange <- offsetRanges) {
            val pstm = conn.prepareStatement("replace into mysqloffset values (?,?,?,?)")
            pstm.setString(1, offsetRange.topic)
            pstm.setInt(2, offsetRange.partition)
            pstm.setLong(3, offsetRange.untilOffset)
            pstm.setString(4, groupId)
            pstm.execute()
            pstm.close()
        }
    }
}
