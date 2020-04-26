package com.tunan.spark.sql.dataframe

import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object rdd2df  extends Logging{

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .config("spark.some.config.option", "some-value")
            .getOrCreate()


        val in = "tunan-spark-sql/data/people.txt"
        val out = "tunan-spark-sql/out"

        spark.sparkContext.setLogLevel("ERROR")

        val sc = spark.sparkContext.textFile(in)
        val mapRDD: RDD[(String, String)] = sc.map(x => {
            val words = x.split(",")
            (words(0), words(1))
        })

        import spark.implicits._
        val df: DataFrame = mapRDD.toDF("name","age")

        df.show()
    }


}