package com.tunan.spark.streaming.state

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

object MapWithState {

    val checkpoint = "./chk_v3"

    def main(args: Array[String]): Unit = {
        // 拿到 StreamingContext
        val ssc = StreamingContext.getOrCreate(checkpoint, functionToCreateContext)
        ssc.start()
        ssc.awaitTermination()
    }

    def functionToCreateContext(): StreamingContext = {
        // 创建 StreamingContext
        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val ssc = new StreamingContext(conf, Seconds(5))
        ssc.checkpoint(checkpoint)

        // 对记录做累加操作
        def mappingFunc = (word: String, one: Option[Int], state: State[Int]) => {
            if (state.isTimingOut()) {
                println("超时3秒没拿到数据")
            } else {
                val sum = one.getOrElse(0) + state.getOption.getOrElse(0)
                val output = (word, sum)
                state.update(sum)
                output
            }
        }

        // 逻辑处理
        val lines = ssc.socketTextStream("hadoop", 9100)
        lines
          .flatMap(_.split(" "))
          .map((_, 1))
          .mapWithState(StateSpec.function(mappingFunc)
            .timeout(Seconds(3))
          ).print()

        ssc
    }
}
