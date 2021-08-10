package com.tunan.spark.sql.mongo

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 收藏表  全量90天
 */

object ods_mongo_cms_house_collect_i {

    def main(args: Array[String]): Unit = {

        val table = "cms_house_collect"

        val conf = new SparkConf()
          .setAppName(this.getClass.getSimpleName)
          //          .setMaster("local[4]")
          //          .set("dfs.client.use.datanode.hostname", "true")
          .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
          .registerKryoClasses(Array(classOf[Collect]))

        val spark = SparkSession.builder()
          .config(conf)
          .enableHiveSupport()
          .getOrCreate()

        var day = ToolUtils.getYesterdayDate
        day = spark.conf.get("spark.executor.time", day) //yyyy-MM-dd
        val start = ToolUtils.getMillisFromDay(day)

        val df = spark
          .read
          .format("com.xhj.external.collect")
          .option("start", start)
          .load()

        //        df.show()

        df
          .write
          .format("orc")
          .mode(SaveMode.Append)
          .saveAsTable(s"crm.ods_mongo_$table")

    }
}
