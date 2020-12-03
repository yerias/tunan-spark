package com.tunan.spark.reinforce

import org.apache.spark.{SparkConf, SparkContext}

object reduceByKey {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val files = sc.textFile("tunan-spark-core/data/job1.txt")
        val mapRDD = files.map(row => {
            val words = row.split(",")
            ((words(0), words(1)), (words(2).trim.toInt, words(3).trim.toInt))
        })

        val reduceByKeyRDD = mapRDD.reduceByKey((x, y) => {
            (x._1+y._1,y._1+y._2)
        })

        reduceByKeyRDD.foreach(println)

        sc.stop
    }
}
