package com.tunan.spark.utils

import org.apache.spark.rdd.RDD

class RichRDD[T](var rdd:RDD[T]) {

    def print(flag:Int = 0): Unit ={
        if (flag == 0){
            rdd.collect().foreach(println)
        }
    }
}
