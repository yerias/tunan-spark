package com.tunan.spark.streaming.directory

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object ScanDirectory {

  def main(args: Array[String]): Unit = {

    // 拿到StreamingContext对象
    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val ssc = new StreamingContext(conf, Seconds(5))

    dispose(ssc)

    //开启StreamingContext
    ssc.start()
    ssc.awaitTermination()
  }

  private def dispose(ssc: StreamingContext): Unit = {
    //文件输入
    val text = ssc.textFileStream("file:///home/hadoop/app/spark/streaming")

    //逻辑处理
    val words = text.flatMap(_.split(" "))
    val pair = words.map(x => (x, 1))
    val result = pair.reduceByKey(_ + _)

    //文件输出
    result.print()
  }
}
