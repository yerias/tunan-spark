package com.tunan.spark.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{RangePartitioner, SparkConf, SparkContext}

import scala.util.Random

object Transformation {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val listRDD1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 5), 3)
        val listRDD2 = sc.parallelize(List(1, 2, 3, 4, 5),1)
        val listRDD3 = sc.parallelize(List(9, 4, 5, 6, 7, 8, 9, 9, 8, 5), 3)
        val listRDD4 = sc.parallelize(List((9, 4), (5, 6), (7, 8), (9, 9), (8, 5)), 3)

        val mapRDD1 = sc.parallelize(List(("1", 18), ("2", 22), ("3", 21), ("4", 26)), 2)
        val mapRDD2 = sc.parallelize(List(("zhaoliu", 18), ("zhangsan", 22), ("list", 21), ("wangwu", 26)), 3)
        val mapRDD3 = sc.parallelize(List(("hongqi", "男"), ("zhangsan", "男"), ("list", "女"), ("wangwu", "男")), 2)
        val mapRDD4 = sc.parallelize(List(("hongqi", "男 18 180"), ("zhangsan", "男 19 170"), ("list", "女 18 160"), ("wangwu", "男 0 0")))

        //        filterByRange(mapRDD1)
        //        flatMapValues(mapRDD4)
        //        sample(listRDD1)
        //        mapPartitionsWithIndex(mapRDD2)
        //        partitionsBy(mapRDD2)
        //        cogroup(mapRDD2, mapRDD3)
        //        join(mapRDD2, mapRDD3)
        //        sortByKey(listRDD3)
        //        sortBy(listRDD3)
        //        coalesce(listRDD4)
        //        repartitionAndSortWithPartitions(listRDD4)
        union(listRDD1, listRDD2)
    }
    def union(rdd1: RDD[Int],rdd2: RDD[Int]): Unit ={
        val unionRDD = rdd1.union(rdd2)
        println(unionRDD.partitions.length) 
        unionRDD.foreach(println)
    }

    def repartitionAndSortWithPartitions(rdd: RDD[(Int, Int)]): Unit = {
        val raswpRDD = rdd.repartitionAndSortWithinPartitions(new RangePartitioner(2, rdd))
        raswpRDD.foreach(println)
    }

    def coalesce(rdd: RDD[Int]): Unit = {
        val coalesceRDD = rdd.coalesce(4, true)
        //        println(coalesceRDD.partitioner.get)
        println(coalesceRDD.partitions.length)
        coalesceRDD.foreachPartition(x => {
            println("-----------------")
            x.foreach(println)
        })
    }

    // 底层调用的sortByKey
    def sortBy(rdd: RDD[(Int)]): Unit = {
        rdd.sortBy(x => x).foreach(println)
    }

    // 先range partitioner分区 再分区内sort
    def sortByKey(rdd: RDD[Int]): Unit = {
        val sortByKeyRDD = rdd.map(x => (x, null)).sortByKey().map((_._1))
        sortByKeyRDD.foreach(println)
    }

    def join(rdd1: RDD[(String, Int)], rdd2: RDD[(String, String)]): Unit = {
        val joinRDD = rdd1.join(rdd2)
        println(joinRDD.partitions.length)
        println(joinRDD.partitioner.get)
        joinRDD.foreach(println)
    }

    def cogroup(rdd1: RDD[(String, Int)], rdd2: RDD[(String, String)]): Unit = {
        val cogroupRDD: RDD[(String, (Iterable[Int], Iterable[String]))] = rdd1.cogroup(rdd2)
        println(cogroupRDD.partitions.length)
        println(cogroupRDD.partitioner.get)
        cogroupRDD.foreach(println)
    }

    def filterByRange(rdd: RDD[(String, Int)]): Unit = {
        val rangeRDD = rdd.partitionBy(new RangePartitioner(2, rdd))
        rangeRDD.filterByRange("2", "4").foreach(println)
    }

    def flatMapValues(rdd: RDD[(String, String)]): Unit = {
        rdd.flatMapValues(x => x.split(" ")).foreach(println)
    }

    def sample(rdd: RDD[Int]): Unit = {
        // 抽取的概率 0.4
        rdd.sample(false, 0.4, new Random().nextInt()).foreach(println)
    }

    def sampleByKey(rdd: RDD[(String, Int)]): Unit = {
        // 指定每个key抽取的概率，不指定会报错
        val map = Map(("zhaoliu", 0.5), ("list", 0.5), ("wangwu", 0.5), ("zhangsan", 0.5))
        rdd.sampleByKey(false, map).foreach(println)
    }

    def mapPartitionsWithIndex(rdd: RDD[(String, Int)]): Unit = {
        // 得到分区index和data
        rdd.mapPartitionsWithIndex({
            (index, iter) => {
                iter.map(x => s"分区${index},数据: ${x._1}_${x._2}")
            }
        }).foreach(println)
    }

    def partitionsBy(rdd: RDD[(String, Int)]): Unit = {
        // value类型的partitioner是None
        // k,v类型的

        //        val parRDD = rdd.partitionBy(new HashPartitioner(2))
        //        val parRDD = rdd.partitionBy(new RangePartitioner(2,rdd))


        println(rdd.partitioner.get)
    }


}