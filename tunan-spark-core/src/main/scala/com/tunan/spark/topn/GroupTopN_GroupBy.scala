package com.tunan.spark.topn

import org.apache.spark.{SparkConf, SparkContext}

object GroupTopN_GroupBy {
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

        val result = mapRDD.reduceByKey(_ + _).groupBy(x => x._1._1).mapValues( x=> x.toList.sortBy(x => -x._2).map(x => (x._1._1,x._1._2,x._2)).take(2))
        result.foreach(println)
    }
}
