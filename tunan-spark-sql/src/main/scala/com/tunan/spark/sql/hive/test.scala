package com.tunan.spark.sql.hive

import org.apache.spark.sql.SparkSession

object test {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().master("local[2]").enableHiveSupport().appName(this.getClass.getSimpleName).getOrCreate()

        spark.sql("select * from default.emp").show()
    }
}
