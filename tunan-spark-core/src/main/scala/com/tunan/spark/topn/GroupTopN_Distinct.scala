package com.tunan.spark.topn

import org.apache.spark.{SparkConf, SparkContext}

object GroupTopN_Distinct {
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

        for (domain <- domains){
            mapRDD.filter( x => domain.equals(x._1._1)).reduceByKey(_+_).sortBy(x => -x._2).take(2).foreach(println)
        }
    }
}
