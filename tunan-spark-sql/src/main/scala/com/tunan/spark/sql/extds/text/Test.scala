package com.tunan.spark.sql.extds.text

import org.apache.spark.sql.{DataFrame, SparkSession}

object Test {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()

    val textDF: DataFrame = spark.read.format("com.tunan.spark.sql.extds.text").load("tunan-spark-sql/extds/emp.txt")

    textDF.printSchema()
    textDF.show()

  }
}
