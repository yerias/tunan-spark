package com.tunan.spark.streaming.transformation

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object BlackList {

  /**
   * 黑名单管理
   * @param args
   */


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)

    val ssc = new StreamingContext(conf, Seconds(5))

    val blacks = List("tunan")
    val blackRDD = ssc.sparkContext.parallelize(blacks)
    val blackMapRDD = blackRDD.map((_,true))

    // 从socket拿到流式数据
    val stream = ssc.socketTextStream("hadoop", 9100)

    // 数据转换成RDD
    stream.transform( rdd => {
      val mapRDD= rdd.map(row => {
        val words = row.split(",")

        (words(0), row)
      })

      val joinRDD: RDD[(String, (String, Option[Boolean]))] = mapRDD.leftOuterJoin(blackMapRDD)

      val filterRDD = joinRDD.filter(_._2._2.getOrElse(false) != true)

      // 返回RDD
      filterRDD.map(_._2._1)

      //输出
    }).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
