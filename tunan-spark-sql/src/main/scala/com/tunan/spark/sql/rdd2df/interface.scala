package com.tunan.spark.sql.rdd2df

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object interface {

    def main(args: Array[String]): Unit = {

        val spark = SparkSession.builder().master("local[2]").appName(this.getClass.getSimpleName).getOrCreate()
        val in = "tunan-spark-sql/data/top.txt"
        val fileRDD: RDD[String] = spark.sparkContext.textFile(in)

        //在原RDD上创建rowRDD
        val mapRDD = fileRDD.map(lines => {
            val words = lines.split(",")
            Row(words(0), words(1), words(2).toDouble)
        })


        //创建和上一步对应的行结构类型的StructType
        val innerStruct =
            StructType(
            StructField("name", StringType, false) ::
            StructField("subject", StringType, false) ::
            StructField("grade", DoubleType, false) :: Nil
            )

        //将schema和Rows结合，创建出DF
        val df = spark.createDataFrame(mapRDD, innerStruct)

        df.show()
    }
}