package com.tunan.spark.reinforce

import com.tunan.spark.reinforce.CollectionAccumulator.Student
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object CacheOrPersist {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val sc = new SparkContext(conf)

        val spark = SparkSession.builder().config(conf).getOrCreate()

        val ssc = new StreamingContext(conf, Seconds(5))

        import spark.implicits._
        val rdd = sc.parallelize(List(Student("zhangsan", 18), Student("lisi", 20), Student("wangwu", 25)))
        rdd.cache() // TODO MEMORY_ONLY
        val ds = rdd.toDS()
        val df = rdd.toDF()
        ds.cache() // TODO MEMORY_AND_DISK
        df.cache() // TODO MEMORY_AND_DISK


        val stream = ssc.socketTextStream("hadoop", 9999)
        stream.cache() // TODO MEMORY_ONLY_SER

    }
}
