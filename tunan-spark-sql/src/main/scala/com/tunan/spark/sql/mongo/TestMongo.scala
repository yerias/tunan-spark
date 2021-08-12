package com.tunan.spark.sql.mongo

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object TestMongo {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf()
            .setAppName(this.getClass.getSimpleName)
        //          .setMaster("local[4]")
        //          .set("dfs.client.use.datanode.hostname", "true")

        val spark = SparkSession.builder()
            .config(conf)
            .enableHiveSupport()
            .getOrCreate()

        val start = ToolUtils.getMillisFromDay("2021-08-10")
        val end = ToolUtils.getMillisFromDay("2021-08-11")
        println("start " + start)
        println("end " + end)

        val fields = "ClassId:string,ProductId:string,Mode:string,ObjectId:string,Status:string,Client:string,UpdateDate:string"


        val df = spark
            .read
            .format("com.wx.mongo")
            .option("start", start)
            .option("end", end)
            .option("fields", fields)
            .load()

        df.show(20,false)

        //        df
        //          .write
        //          .format("orc")
        //          .mode(SaveMode.Append)
        //          .saveAsTable(s"crm.ods_mongo_$table")

    }
}
