package com.tunan.spark.reinforce

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object flatMap {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val file = sc.textFile("tunan-spark-core/data/job2.txt")

        val flatMap = file.flatMap(row => {
            val lines = row.split(",")
            val user = lines(0)
            val list = lines(1)
            val view = lines(2)
            val click = lines(3)
            val lists = list.split("\\|")
            // 这里使用map处理返回的是数组，但是我们的结果不要是数组的，所以使用flatMap把数组扁平化
            val result: Array[((String, String), (Int, Int))] = lists.map(x => {
                ((user, x), (view.trim.toInt, click.trim.toInt))
            })
            result
        })

        implicit val ord = Ordering[Int].on[((String, String), (Int, Int))](x => x._2._1)

        flatMap.reduceByKey((x,y) => {
            (x._1+y._1,x._2+y._2)
        }).sortBy(x => x).foreach(println)

        sc.stop()
    }
}