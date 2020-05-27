package com.tunan.spark.sql.rdd2df

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object reflect {

    def main(args: Array[String]): Unit = {

        val spark = SparkSession.builder().master("local[2]").appName(this.getClass.getSimpleName).getOrCreate()

        import spark.implicits._
        val in = "tunan-spark-sql/data/top.txt"
        val fileRDD: RDD[String] = spark.sparkContext.textFile(in)
        val mapRDD: RDD[people] = fileRDD.map(lines => {
            val words = lines.split(",")
            people(words(0), words(1), words(2).toInt)
        })

        mapRDD.toDF().printSchema()
    }

    case class people(name:String,subject:String,grade:Int)
}
