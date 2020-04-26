package com.tunan.spark.streaming.state

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object UpdateState {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val ssc = new StreamingContext(conf, Seconds(5))

    dispose(ssc)

    ssc.start()
    ssc.awaitTermination()
  }

  private def dispose(ssc: StreamingContext) = {
    val lines = ssc.socketTextStream("hadoop", 9100)

    ssc.checkpoint("./chk")
    lines.flatMap(_.split(" ")).map((_, 1)).updateStateByKey(updateFunction)
      .print()
  }

  def updateFunction(newValues: Seq[Int], oldValues: Option[Int]): Option[Int] = {
    val curr = newValues.sum
    val old = oldValues.getOrElse(0)
    val count = curr + old
    Some(count)
  }
}
