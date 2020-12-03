package com.tunan.spark.reinforce

import org.apache.spark.{SparkConf, SparkContext}

object CollectionAccumulator {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val collAcc = sc.collectionAccumulator[Student]("collect acc")

        val rdd = sc.parallelize(List(Student("zhangsan", 18), Student("lisi", 20), Student("wangwu", 25)))
        rdd.foreach(row => {
            collAcc.add(row)
        })

        println(collAcc.value)
    }

    case class Student(name:String,age:Int)
}
