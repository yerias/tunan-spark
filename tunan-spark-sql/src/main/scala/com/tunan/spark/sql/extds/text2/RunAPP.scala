package com.tunan.spark.sql.extds.text2

import org.apache.spark.sql.SparkSession

object RunAPP {

    def main(args: Array[String]): Unit = {

        val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()

        val in = "tunan-spark-sql/extds/text_access/part-*"

        // 最最最关键的是 format指定的格式不能是text，而是DefaultSource的报名，并且只能命名为DefaultSource，源码中写死
        val textDf = spark.read.format("com.tunan.spark.sql.extds.text2").load(in)

        textDf.printSchema()
        textDf.show(true)
    }
}
