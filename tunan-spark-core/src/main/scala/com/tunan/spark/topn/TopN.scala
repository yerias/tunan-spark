package com.tunan.spark.topn

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object TopN {

    //10亿条ipv4数据，用8G内存，单线程找出频次最高的100条。

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getSimpleName)
        val sc = new SparkContext(conf)

        val in = "tunan-spark-core/data/ipv4.txt"
        val text = sc.textFile(in)

        val top: Array[(String, Long)] = text.map(_.trim).map((_, 1L)).reduceByKey(_ + _).sortBy(x => -x._2).take(3)
        top.foreach(println)
    }
}
