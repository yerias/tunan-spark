package com.tunan.doris.jdbc

import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.collection.mutable.ListBuffer

/**
 * @Auther: 李沅芮
 * @Date: 2021/12/30 11:56
 * @Description: 从执行完到数据能查出来延迟大概5s左右
 */
object SparkWrite {

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

        import spark.implicits._

        val buffer = new ListBuffer[Student]()
        buffer += Student(8, "小七", "2021", "10", "27")

        val df = buffer.toDF()

        df.write
            .format("jdbc")
            .mode(SaveMode.Append)
            .option("url", url)
            .option("dbtable", "student_stream_load")
            .option("user", username)
            .option("password", password)
            .option("driver", driver)
            .save()

    }

    case class Student(id: Int, name: String, year: String, month: String, day: String)

}
