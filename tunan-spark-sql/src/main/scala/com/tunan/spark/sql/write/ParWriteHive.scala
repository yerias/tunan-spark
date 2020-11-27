package com.tunan.spark.sql.write

import org.apache.spark.sql.{SaveMode, SparkSession}

object ParWriteHive {

    def main(args: Array[String]): Unit = {

        System.setProperty("HADOOP_USER_NAME", "hadoop")

        val spark = SparkSession.builder()
          .master("local[4]")
          .appName(this.getClass.getSimpleName)
          .config("hive.exec.dynamic.partition.mode", "nonstrict")
          .config("dfs.client.use.datanode.hostname", "true")
          .enableHiveSupport()
          .getOrCreate()

        val logins = List(
            Login(10001, "y10001", "2020-11-27"),
            Login(10002, "y10002", "2020-11-27"),
            Login(10003, "y10003", "2020-11-27"),
            Login(10004, "y10004", "2020-11-27"),
            Login(10002, "z10002", "2020-11-28"),
            Login(10003, "z10003", "2020-11-28"),
            Login(10005, "z10005", "2020-11-28"),
            Login(10005, "z10005", "2020-11-28"))

        import spark.implicits._
        val ds = logins.toDS()

        // insertInto 表必须存在，且如果是分区表没必要再指定partitionBy() [推介]
        // saveAsTable 表如果存在会删除后重建，普通表可用，分区表没有分区字段(会报错)
        ds.where('login_time === "2020-11-28").coalesce(1)
          .write
          .format("hive")
          .mode(SaveMode.Overwrite)
//          .partitionBy("login_time")
          .insertInto("default.login")

        spark.stop()
    }
}

case class Login(user_id:Int,user_name:String,login_time:String)