package com.tunan.spark.transformation

import org.apache.spark.{SparkConf, SparkContext}

object RepartitionAndCoalesce {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local").setAppName(RepartitionAndCoalesce.getClass.getName)

        val sc = new SparkContext(conf)

        val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)

        val rdd2 = rdd.repartition(2)
        println(rdd2.partitions.length)

        var list = List(1, 2, 3, 4, 5, 6, 7, 8, 9)

        val rdd3 = rdd.coalesce(1)
        println(rdd3.partitions.length)
    }
}
