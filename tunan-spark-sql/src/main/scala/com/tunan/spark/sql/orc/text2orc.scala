package com.tunan.spark.sql.orc

import org.apache.spark.sql.SparkSession

object text2orc {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .config("spark.some.config.option", "some-value")
            .getOrCreate()
        val in = "tunan-spark-sql/data/people.txt"
        val out = "tunan-spark-sql/out"

        val df = spark.read.textFile(in)

        import spark.implicits._
        df.map(row => {
            val words = row.split(",")
            (words(0),words(1))
        })
            .toDF("name","age")
            .write
            .mode("overwrite")
            .format("orc")
            .save(out)
    }
}
