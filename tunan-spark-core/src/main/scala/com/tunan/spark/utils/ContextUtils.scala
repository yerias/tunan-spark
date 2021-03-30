package com.tunan.spark.utils

import org.apache.spark.{SparkConf, SparkContext}

object ContextUtils {

    def getSparkContext(conf:SparkConf): SparkContext ={
        new SparkContext(conf)
    }

    def getSparkContext(appName:String,master:String = "local[2]"): SparkContext ={
        val conf = new SparkConf().setMaster(master).setAppName(appName)
        new SparkContext(conf)
    }
}