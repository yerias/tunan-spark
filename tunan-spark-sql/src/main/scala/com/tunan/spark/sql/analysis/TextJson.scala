package com.tunan.spark.sql.analysis

import org.apache.spark.sql.{DataFrame, SparkSession}

object TextJson {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()

    val jsonDF: DataFrame = spark.read.option("mode","DROPMALFORMED").json("tunan-spark-sql/data/test.json")

    jsonDF.show()

  }
}
