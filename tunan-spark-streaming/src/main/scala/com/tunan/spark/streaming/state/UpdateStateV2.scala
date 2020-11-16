package com.tunan.spark.streaming.state

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object UpdateStateV2 {

  val checkpoint = "./chk_v2"
  def main(args: Array[String]): Unit = {

    // 拿到StreamingContext
    val ssc  = StreamingContext.getOrCreate(checkpoint, functionToCreateContext _)
    ssc.start()
    ssc.awaitTermination()
  }

  // 创建StreamingContext
  def functionToCreateContext(): StreamingContext = {
    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val ssc = new StreamingContext(conf,Seconds(5))   // new context

    dispose(ssc)

    ssc.checkpoint(checkpoint)   // set checkpoint directory
    ssc
  }

  // 处理具题的业务逻辑
  private def dispose(ssc: StreamingContext): Unit = {
    val lines = ssc.socketTextStream("hadoop", 9100) // create DStreams

    lines
      .flatMap(_.split(" "))
      .map((_, 1))
      .updateStateByKey(updateFunction)
      .print()
  }

  // 更新state
  def updateFunction(newValues: Seq[Int], oldValues: Option[Int]): Option[Int] = {
    val curr = newValues.sum
    val old = oldValues.getOrElse(0)
    val count = curr + old
    Some(count)
  }
}
