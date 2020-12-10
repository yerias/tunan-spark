package com.tunan.clickhouse

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object Spark2ClickHouse {

    def save(df: DataFrame): Unit = {


        val properties = new Properties
        properties.put("socket_timeout", "300000") // 设置超时时间
        properties.put("rewriteBatchedStatements", "true") // 启动批处理


        df.write
          .mode(SaveMode.Append) // 只能设置Append，不会自动创建表
          .option("batchsize", "100") // 重要参数,设置写入批次大小
          .option("isolationLevel", "NONE") // 重要参数,不设置事务写入
          .option("numPartitions", "2") // 重要参数，设置并行度
          .option("driver", "ru.yandex.clickhouse.ClickHouseDriver")
          .jdbc("jdbc:clickhouse://aliyun:8123/default", "url_access", properties)
    }

    def select(spark: SparkSession): DataFrame = {
        val df: DataFrame = spark.read
          .format("jdbc")
          .option("driver", "ru.yandex.clickhouse.ClickHouseDriver")
          .option("url", "jdbc:clickhouse://aliyun:8123/default")
          .option("dbtable", "url_access")
          .load()
        df
    }

    def main(args: Array[String]): Unit = {

        val spark = SparkSession.builder()
          .master("local[3]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()
        import spark.implicits._


        val files = spark.sparkContext.textFile("tunan-spark-clickhouse/data/url.txt")
        val df = files.map(row => {
            val words = row.split("\t")
            Url(words(0), words(1))
        }).repartition(3).toDF()

        //        select(spark).show()
        //        save(df)

        spark.stop()
    }

    case class Emp(emp_id: Int, name: String, work_place: String, age: Int, depart: String, salary: Double)

    case class Url(url: String, domain: String)

}
