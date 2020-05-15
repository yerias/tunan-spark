package com.tunan.spark.broadcast

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object BroadcastApp {
    def main(args: Array[String]): Unit = {
        val sc: SparkContext = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        // Fact table  航线(起点机场, 终点机场, 航空公司, 起飞时间)
        val flights = sc.textFile("tunan-spark-core/broadcast/flights.txt")

        // Dimension table 机场(简称, 全称, 城市, 所处城市简称)
        val airports: RDD[String] = sc.textFile("tunan-spark-core/broadcast/airports.txt")

        // Dimension table  航空公司(简称,全称)
        val airlines = sc.textFile("tunan-spark-core/broadcast/airlines.txt")

        /**
         * 最终统计结果：
         * 出发城市           终点城市           航空公司名称         起飞时间
         * Seattle           New York       Delta Airlines          7:00
         * San Francisco     Los Angeles    American Airlines       7:05
         * San Francisco     New York       Virgin America          7:05
         * New York          Los Angeles    Delta Airlines          7:10
         * Los Angeles       Seattle        Delta Airlines          7:10
         */

        //广播Dimension Table
        val airportsBC = sc.broadcast(airports.map(x => {
            val words = x.split(",")
            (words(0), words(2))
        }).collectAsMap())

        val airlinesBC = sc.broadcast(airlines.map(x => {
            val words = x.split(",")
            (words(0), words(1))
        }).collectAsMap())

        flights.map(lines => {
            val words = lines.split(",")
            val a = airportsBC.value(words(0))
            val b = airportsBC.value(words(1))
            val c = airlinesBC.value(words(2))
            a+"    "+b+"    "+c+"    "+words(3)
        }).foreach(println)
        sc.stop()
    }
}