package com.tunan.spark.reinforce

import org.apache.spark.{SparkConf, SparkContext}

object Aggregate {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
        val agg = rdd.aggregate(1)(_ + _, _ + _)

        rdd.sum()

        sc.stop()
    }
}
