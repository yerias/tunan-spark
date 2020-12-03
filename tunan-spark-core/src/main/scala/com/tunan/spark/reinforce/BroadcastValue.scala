package com.tunan.spark.reinforce

import org.apache.spark.{SparkConf, SparkContext}

object BroadcastValue {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val list = List(("zhangsan", "xiaozhang"), ("lisi", "laoli"), ("wangwu", "xiaowang"))
        val list2 = List(("zhangsan", 18), ("lisi", 20), ("wangwu", 25))
        val rdd = sc.parallelize(list2)


        val broadcastMap = sc.broadcast(sc.parallelize(list).collectAsMap())

        rdd.foreach(row => {
            val value = broadcastMap.value.getOrElse(row._1, "other")
            println((value, row._2))
        })

        sc.stop()
    }
}
