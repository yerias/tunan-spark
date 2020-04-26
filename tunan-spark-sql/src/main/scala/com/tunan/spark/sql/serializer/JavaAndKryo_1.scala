package com.tunan.spark.sql.serializer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable.ArrayBuffer

case class Info(track_time:String, url:String, session_id:String, referer:String, ip:String, end_user_id:String, city_id:String )

object JavaAndKryo_1 {

    def main(args: Array[String]): Unit = {

        val in = "file:///home/hadoop/data/video.txt"

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)

        val sc = new SparkContext(conf)

        val fileRDD = sc.textFile(in)

        /*val arr = new ArrayBuffer[Info]
        files.map(row => {
            val words = row.split("\t")
            arr += Info(words(0),words(1),words(2),words(3),words(4),words(5),words(6))
        })

        val rdd = sc.parallelize(arr)*/

        val perRDD = fileRDD.persist(StorageLevel.MEMORY_ONLY).count()


        Thread.sleep(Int.MaxValue)

        sc.stop()
    }
}
