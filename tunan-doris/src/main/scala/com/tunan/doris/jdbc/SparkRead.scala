package com.tunan.doris.jdbc

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Auther: 李沅芮
 * @Date: 2021/12/30 11:56
 * @Description:
 */
object SparkRead {

    val url = s"jdbc:mysql://39.96.28.178:19030/test"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val password = "root"

    def main(args: Array[String]): Unit = {

        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .getOrCreate()

        val df: DataFrame = spark.read
            .format("jdbc")
            .option("url", url)
            .option("dbtable", "student_stream_load")
            .option("user", username)
            .option("password", password)
            .option("driver", driver)
            .load()

        df.show()
    }
}
