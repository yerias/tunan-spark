package com.tunan.spark.sql.push

import org.apache.spark.sql.SparkSession

object QueryPush {

    def main(args: Array[String]): Unit = {

            val spark = SparkSession
                  .builder()
                  .master("local[2]")
                  .appName(this.getClass.getSimpleName)
                  .getOrCreate()


        val tmp_sql =
            """
              |(SELECT id,name,age
              |FROM study.student
              |WHERE id > 14) as t
              |
              |""".stripMargin

        val df = spark.read.format("jdbc")
          .option("url", "jdbc:mysql://aliyun:3306/study")
          .option("driver", "com.mysql.jdbc.Driver")
          .option("query", tmp_sql)
          .option("user", "root")
          .option("password", "juan970907!@#")
//          .option("partitionColumn", "id")
//          .option("lowerBound", 1)
//          .option("upperBound", 10)
//          .option("numPartitions", 3)
          .load()


        df.show()

        spark.stop()

    }
}
