package com.tunan.spark.wc

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.{SparkConf, SparkContext}

object wordcount {

    def main(args: Array[String]): Unit = {

        val (in,out) = (args(0),args(1))

        val sc = ContextUtils.getSparkContext(wordcount.getClass.getSimpleName)

        val result = sc.textFile(in).flatMap(_.split("\t")).map((_, 1)).reduceByKey(_ + _)
        result.saveAsTextFile(out)
    }
}
