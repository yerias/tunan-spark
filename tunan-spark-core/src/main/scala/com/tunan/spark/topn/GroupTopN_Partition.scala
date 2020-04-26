package com.tunan.spark.topn

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


object GroupTopN_Partition {
    def main(args: Array[String]): Unit = {
        val in = "tunan-spark-core/data/site.log"
        //连接SparkMaster
        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val fileRDD = sc.textFile(in)

        val mapRDD = fileRDD.map(lines => {
            val words = lines.split("\t")
            ((words(0), words(1)), 1)
        })

        val domains = mapRDD.map(x => x._1._1).distinct().collect()

        val mapPartRDD = mapRDD.reduceByKey(new MyPartitioner(domains), _ + _).mapPartitions(partition => {
            partition.toList.sortBy(x => -x._2).take(2).iterator
        })

        mapPartRDD.foreach(println)
    }
}