package com.tunan.spark.sql.analysis

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object CoreTopN {

    def main(args: Array[String]): Unit = {

        val sparkConf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val sc = new SparkContext(sparkConf)

        val input = "tunan-spark-sql/data/site.log"
        val rdd = sc.textFile(input)

        val topN = 2
        rdd.map(x=>{
            val splits = x.split("\t")
            val domain = splits(0)
            val url = splits(1)

            ((domain,url),1)
        }).reduceByKey(_+_)
            .groupBy(_._1._1)
            .mapValues(x=>{
                x.toList.sortBy(-_._2).map(x=>(x._1._2,x._2)).take(topN)
            })
            .foreach(println)

        sc.stop()
    }
}
