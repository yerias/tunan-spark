package com.tunan.spark.streaming.output

import com.tunan.spark.utils.mysql.MySQLUtils
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import sun.util.logging.resources.logging

object DStreamOut extends Logging{

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)

    val ssc = new StreamingContext(conf, Seconds(5))

    // 读取内容
    val lines = ssc.socketTextStream("hadoop", 9100)

    // 逻辑转换
    val dstream: DStream[(String, Long)] = lines.flatMap(_.split(" ")).countByValue()

    // 写出内容
    dstream.foreachRDD( rdd => {
      rdd.foreachPartition(partition => {
        MySQLUtils.connect()
        partition.foreach(
          fields => {
            val name = fields._1
            val cnt = fields._2
//            MySQLUtils.insert(s"insert into dstream(name,age) values('${name}',${cnt})")
            println(s"$name  $cnt")
          }
        )
        MySQLUtils.close()
      })
    })
/*    dstream.foreachRDD { rdd =>
      rdd.foreachPartition { partition =>
        MySQLUtils.insert(s"insert into dstream(name,age) values(?,?)",fields._1,fields._2.toInt)
      }
    }*/



    ssc.start()
    ssc.awaitTermination()
  }
}
