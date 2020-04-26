package com.tunan.spark.sql.dataframe

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object text2df {

    def main(args: Array[String]): Unit = {

        val in = "tunan-spark-sql/data/people.txt"
        val out = "tunan-spark-sql/out"

        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .config("spark.some.config.option", "some-value")
            .getOrCreate()
        import spark.implicits._

        //DataFrame不能直接split，且调用map返回的是一个Dataset
        val df: DataFrame = spark.read.format("text").load(in)
        val mapDF = df.map(row => {
            val words = row.toString().split(",")
            words(0) +","+words(1)
        })
        /*
        mapDF.show()

        //DataFrame转换为RDD后，返回的是一个RDD
        val mapRDD2DF: DataFrame = df.rdd.map(row => {
            val words = row.toString().split(",")
            (words(0), words(1))
        }).toDF()
        mapRDD2DF.show()

        //使用textFile方法读取文本文件直接返回的是一个Dataset
        val ds: Dataset[String] = spark.read.textFile(in)
        val mapDs: Dataset[(String, String)] = ds.map(row => {
            val words = row.split(",")
            (words(0), words(1))
        })
        mapDs.show()*/

        mapDF.write
            .format("text")
            // 添加压缩操作
            .option("compression","gzip")
            .mode("overwrite")
            .save(out)
    }
}
