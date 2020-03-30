package com.tunan.spark.utils

import org.apache.spark.rdd.RDD

object ImplicitAspect {

    //增强RDD 传入普通RDD 返回增强RDD ==> new 增强RDD
    implicit def rdd2RichRDD[T](rdd:RDD[T]):RichRDD[T] = new RichRDD[T](rdd)
}
