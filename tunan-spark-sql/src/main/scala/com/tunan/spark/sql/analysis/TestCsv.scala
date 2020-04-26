package com.tunan.spark.sql.analysis

import org.apache.spark.sql.{DataFrame, SparkSession}

object TestCsv {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()

    val csvDF: DataFrame = spark.read
      .format("csv")
      .option("header", "true")
      .option("sep", "|")
      .option("inferSchema","true")
      .load("tunan-spark-sql/data/test.csv")

    csvDF.printSchema()
    csvDF.show()
  }
}
