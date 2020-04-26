package com.tunan.spark.wc

import com.tunan.spark.utils.hadoop.CheckHDFSOutPath
import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}

object WC {

    def main(args: Array[String]): Unit = {

        val in = args(0)
        val out = args(1)

        val conf = new SparkConf()
            .setAppName(this.getClass.getSimpleName)
            .setMaster("local[2]")

        //拿到sc
        val sc = new SparkContext(conf)


        //删除输出目录
       // CheckHDFSOutPath.ifExistsDeletePath(sc.hadoopConfiguration, out)

        //操作算子
        val result = sc.textFile(in).flatMap(_.split("\t")).map((_, 1)).reduceByKey(_ + _)

        //保存文件
        result.saveAsTextFile(out)

        sc.stop()
    }
}
