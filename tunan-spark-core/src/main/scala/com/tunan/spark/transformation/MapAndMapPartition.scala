package com.tunan.spark.transformation

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object MapAndMapPartition {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local").setAppName("MapAndMapPartition")
        val sc = new SparkContext(conf)
        sc.setLogLevel("Error")
        val rdd = sc.parallelize(List(1, 2, 3, 4, 5,11, 22, 33, 44, 55),2)

        rdd.map(x => {
            println("初始化连接。。。")
            println("正在操作 "+x+" 。。。")
            println("关闭连接。。。")
        }).collect()


        println("-------------------------------------")


        rdd.mapPartitions(partition => {
            val list = new ArrayBuffer[Integer]()
            println("初始化连接。。。")
            partition.foreach(x =>{
                list.append(x)
                println("正在操作 "+x+" 。。。")
            })
            println("关闭连接。。。")
            list.toIterator
        }).collect()


    }
}
