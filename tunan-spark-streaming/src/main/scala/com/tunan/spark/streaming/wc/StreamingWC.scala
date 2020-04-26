package com.tunan.spark.streaming.wc

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingWC {

  def main(args: Array[String]): Unit = {
    // 拿到ssc
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(5))

    //业务逻辑
    val lines = ssc.socketTextStream("hadoop", 9100)
/*    val words = lines.flatMap(_.split(" "))
    val paris: DStream[(String, Int)] = words.map(x => (x, 1))
    val result = paris.reduceByKey(_ + _)
    result.print()*/

    //记录行数
//    lines.count().print()

    //每行的元素数量
//    lines.flatMap(_.split(" ")).count().print()

    //每个元素出现的次数
    lines.flatMap(_.split(" ")).countByValue().print()

    //开启ssc
    ssc.start()
    ssc.awaitTermination()
  }
}
