package com.tunan.spark.sql.dataframe

import org.apache.spark.sql.{DataFrame, SparkSession}

object csv2df {
    def main(args: Array[String]): Unit = {
        val in = "tunan-spark-sql/data/people.csv"

        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .getOrCreate()

        val df: DataFrame = spark.read
            .format("csv")
            .option("header", "true")
            .option("sep", ";")
            .option("inferSchema","true")
            .load(in)
        df.show()

    }
}
