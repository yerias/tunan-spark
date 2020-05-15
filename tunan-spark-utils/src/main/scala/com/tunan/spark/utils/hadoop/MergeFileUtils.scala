package com.tunan.spark.utils.hadoop

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object MergeFileUtils {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getSimpleName)

        val sc = new SparkContext(conf)

        val path = "tunan-spark-sql/extds/text_access/part-*"
        val out = "tunan-spark-sql/extds/etl-access"


/*
        val rdd = sc.newAPIHadoopFile(path,classOf[LzoTextInputFormat],classOf[LongWritable],classOf[Text])
          .map(_._2.toString)
*/


        /*
                val configuration = sc.hadoopConfiguration
                configuration.set(FileOutputFormat.COMPRESS , "true")
                configuration.set(FileOutputFormat.COMPRESS_CODEC,"com.hadoop.mapreduce.LzoTextInputFormat")*/


/*        val configuration = new Configuration()
        configuration.set("io.compression.codecs",
            "org.apache.hadoop.io.compress.DefaultCodec,org.apache.hadoop.io.compress.GzipCodec,com.hadoop.compression.lzo.LzopCodec")
        configuration.set("io.compression.codec.lzo.class",
            "com.hadoop.compression.lzo.LzoCodec") //从hdfs中读取数据 val lines: RDD[String] =
        sc.newAPIHadoopFile(path, classOf[LzoTextInputFormat], classOf[LongWritable],
            classOf[Text], configuration).map(x => x._2.toString) //获得到rdd*/




        sc.textFile(path).take(10).foreach(println)

    }
}
