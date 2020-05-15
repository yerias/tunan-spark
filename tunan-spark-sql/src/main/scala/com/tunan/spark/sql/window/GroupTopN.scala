package com.tunan.spark.sql.window

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object GroupTopN {

  def main(args: Array[String]): Unit = {

      val spark = SparkSession
        .builder()
        .master("local[2]")
        .appName(this.getClass.getSimpleName)
        .getOrCreate()


    val date = Seq(
      ("Thin",       "cell phone", 6000),
      ("Normal",     "tablet",     1500),
      ("Mini",       "tablet",     5500),
      ("Ultra thin", "cell phone", 5000),
      ("Very thin",  "cell phone", 6000),
      ("Big",        "tablet",     2500),
      ("Bendable",   "cell phone", 3000),
      ("Foldable",   "cell phone", 3000),
      ("Pro",        "tablet",     4500),
      ("Pro2",       "tablet",     6500))

    val dataRDD: RDD[(String, String, Int)] = spark.sparkContext.parallelize(date)

    val productRow: RDD[Row] = dataRDD.map(row => {
      Row(row._1,row._2,row._3.toLong)
    })

    val schema = StructType(
      List(
        StructField("product", StringType),
        StructField("category", StringType),
        StructField("revenue", LongType)
      )
    )

    val df = spark.createDataFrame(productRow, schema)


    //在每个类别中最畅销和第二畅销的产品是什么?
    import org.apache.spark.sql.expressions.Window
    import spark.implicits._
    // 1. 分组 排序
    val windowSpec = Window.partitionBy('category).orderBy('revenue.desc)
    import org.apache.spark.sql.functions._
    // 2. 开窗
    val win: DataFrame = df.withColumn("rank", row_number().over(windowSpec))
    // 3. 过滤
    val filter =win.where('rank <= 2)

    filter.show()
  }
}
