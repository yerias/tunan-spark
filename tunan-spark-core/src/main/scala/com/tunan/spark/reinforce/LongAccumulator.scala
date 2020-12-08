package com.tunan.spark.reinforce

import org.apache.spark.{SparkConf, SparkContext}

object LongAccumulator {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val longAcc = sc.longAccumulator("Long Acc")
        val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7))

        val mapRDD = rdd.map(row => {
            longAcc.add(1L)
        })

        mapRDD.collect()
//        mapRDD.count()

        println(longAcc.value)
    }
}
