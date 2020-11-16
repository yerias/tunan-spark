package com.tunan.spark.sql.extds.hbase

import org.apache.spark.sql.SparkSession

object SparkHBaseSourceApp {

    // 主类
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()
        val df = spark.read.format("com.tunan.spark.sql.extds.hbase")
          .option("hbase.table.name", "student")
          .option("spark.table.schema", "(adress string,age int,email string,girlfriend string ,love string,name string,sex string)")
          .option("spark.zookeeper.host.port", "hadoop:2181")
          .option("spark.select.cf","info")
          .load()

        df.printSchema()
//        df.show()
        df.createOrReplaceTempView("student")
        spark.sql("select * from student where age > 18").show(false)
    }
}
