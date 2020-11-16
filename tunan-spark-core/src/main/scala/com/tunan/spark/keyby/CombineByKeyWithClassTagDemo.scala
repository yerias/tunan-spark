package com.tunan.spark.keyby

import org.apache.spark.{SparkConf, SparkContext}

object CombineByKeyWithClassTagDemo {

    def main(args: Array[String]): Unit = {


        val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getSimpleName)
        val sc = new SparkContext(conf)

        //        val rdd = sc.parallelize(List((1, 3), (1, 4), (1, 2), (2, 3), (3, 6), (3, 8)), 3)
        //
        //        // 求和
        //        val result = rdd.combineByKey(
        //            x => x,
        //            (a: Int, b: Int) => a + b,
        //            (x: Int, y: Int) => x + y
        //        ).collect()

        //        println(result.mkString)

        //        // 求平均值
        //        val rdd = sc.parallelize(List(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98)), 2)
        //        val result = rdd.combineByKey(
        //            (_, 1),
        //            (a: (Int, Int), b: Int) => (a._1 + b, a._2 + 1),
        //            (x: (Int, Int), y: (Int, Int)) => (x._1 + y._1, x._2 + y._2)
        //        ).map{
        //            case (key, value) => {
        //                (key,(value._1/value._2).toDouble)
        //            }
        //        }.collect()
        //
        //        println(result.mkString)

        val rdd = sc.parallelize(List((1, 3), (1, 4), (1, 2), (2, 3), (3, 6), (3, 8)), 3)

        // 求和
        val result = rdd.combineByKeyWithClassTag(
            x => x,
            (a: Int, b: Int) => a + b,
            (x: Int, y: Int) => x + y
        ).collect()

        println(result.mkString)
    }
}
