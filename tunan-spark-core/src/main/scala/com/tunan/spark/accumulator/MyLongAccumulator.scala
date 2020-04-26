package com.tunan.spark.accumulator

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD

object MyLongAccumulator {
    def main(args: Array[String]): Unit = {
        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)
        //生成计数器
        val acc = sc.longAccumulator("计数")
        val rdd = sc.parallelize(1 to 8)
        val forRDD = rdd.map(x => {
            //计数器做累加
            acc.add(1L)
        })

        forRDD.cache().count()
        println(acc.value)
        forRDD.count()
        println(acc.value)
        sc.stop()
    }
}
