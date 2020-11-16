package com.tunan.spark.streming.kafka.hbase

import com.tunan.spark.streming.kafka.offmanager.HBaseOffsetsManager
import com.tunan.spark.streming.kafka.utils.{HBaseCRUDUtils, HBaseUtils}
import org.apache.hadoop.hbase.client.{HTable, Put}
import org.apache.hadoop.hbase.{Cell, CellUtil, TableName}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object UserView {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val ssc = new StreamingContext(conf, Seconds(5))

        val groupId = "view_group"

        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "hadoop:9090,hadoop:9091,hadoop:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> groupId,
            "auto.offset.reset" -> "earliest", //latest
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )

        val topics = Array("view_topic")

        val fromOffsets = HBaseOffsetsManager.obtainOffsets(topics, groupId)

        val kafkaStream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams, fromOffsets)
        )

        var offsetRanges: Array[OffsetRange] = Array.empty[OffsetRange]

        val stream = kafkaStream.transform(rdd => {

            // 利用transform取得OffsetRanges
            offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

            rdd.map(row => row.value())
        })


        // 不能直接在stream上操作 必须先拿到HasOffsetRanges，才能开始业务逻辑
        stream.foreachRDD { rdd =>
            rdd.map(x => (x, 1)).reduceByKey(_ + _)
              .foreachPartition(partition => {
                  val conn = HBaseUtils.getConnection
                  partition.foreach(row => {

                      val key = row._1
                      val value = row._2.toInt

                      val cell: Cell = HBaseCRUDUtils.get("user",key).rawCells()(0)
                      val cellValue = Bytes.toString(CellUtil.cloneValue(cell))
                      val result = value + cellValue.toInt

                      println(key + "  " + result)
                      HBaseUtils.set(conn, TableName.valueOf("user"),Bytes.toBytes(key),Bytes.toBytes("info"),Bytes.toBytes("cn"),Bytes.toBytes(""+result))

                      HBaseOffsetsManager.storeOffsets(offsetRanges, groupId)

                      HBaseUtils.release(conn)
                  })
              })
        }


        ssc.start()
        ssc.awaitTermination()


    }
}