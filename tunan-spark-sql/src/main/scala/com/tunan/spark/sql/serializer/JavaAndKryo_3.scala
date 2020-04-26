package com.tunan.spark.sql.serializer

import org.apache.spark.{SparkConf, SparkContext}

import org.apache.spark.storage.StorageLevel
import scala.collection.mutable.ArrayBuffer

object JavaAndKryo_3 {

    def main(args: Array[String]): Unit = {

        val in = "tunan-spark-sql/data/video.txt"

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)

        val sc = new SparkContext(conf)

        val files = sc.textFile(in)

        val arr = new ArrayBuffer[Info]
        files.map(row => {
            val words = row.split("\t")
            arr += Info(words(0),words(1),words(2),words(3),words(4),words(5),words(6))
        })

        val rdd = sc.parallelize(arr)


        rdd.persist(StorageLevel.MEMORY_ONLY_SER).count()
        Thread.sleep(Int.MaxValue)
        sc.stop()
    }
}
