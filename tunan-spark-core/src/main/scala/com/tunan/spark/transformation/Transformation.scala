package com.tunan.spark.transformation

import org.apache.spark.{HashPartitioner, RangePartitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.util.Random

object Transformation {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val listRDD = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9),3)
        val listRDD2 = sc.parallelize(List(1, 2, 3, 4, 5))
        val listRDD3 = sc.parallelize(List(4, 5, 6, 7, 8, 9, 9, 9, 8, 5))

        val mapRDD = sc.parallelize(List(("1", 18), ("2", 22), ("3", 21), ("4", 26)),2)
        val mapRDD2 = sc.parallelize(List(("zhaoliu", 18), ("zhangsan", 22), ("list", 21), ("wangwu", 26)),3)
        val mapRDD3 = sc.parallelize(List(("hongqi", "男"), ("zhangsan", "男"), ("list", "女"), ("wangwu", "男")))
        val mapRDD4 = sc.parallelize(List(("hongqi", "男 18 180"), ("zhangsan", "男 19 170"), ("list", "女 18 160"), ("wangwu", "男 0 0")))

//        filterByRange(mapRDD)
//        flatMapValues(mapRDD4)
//        sample(listRDD)
//        mapPartitionsWithIndex(mapRDD2)
        partitionsBy(mapRDD2)

    }


    def filterByRange(rdd:RDD[(String,Int)]): Unit ={
        val rangeRDD = rdd.partitionBy(new RangePartitioner(2, rdd))
        rangeRDD.filterByRange("2","4").foreach(println)
    }

    def flatMapValues(rdd:RDD[(String,String)]): Unit ={
        rdd.flatMapValues(x => x.split(" ")).foreach(println)
    }

    def sample(rdd:RDD[Int]): Unit ={
        // 抽取的概率 0.4
        rdd.sample(false,0.4,new Random().nextInt()).foreach(println)
    }

    def sampleByKey(rdd:RDD[(String,Int)]): Unit ={
        // 指定每个key抽取的概率，不指定会报错
        val map = Map(("zhaoliu", 0.5), ("list", 0.5), ("wangwu", 0.5), ("zhangsan", 0.5))
        rdd.sampleByKey(false,map).foreach(println)
    }

    def mapPartitionsWithIndex(rdd:RDD[(String,Int)]): Unit ={
        // 得到分区index和data
        rdd.mapPartitionsWithIndex({
            (index,iter) => {
                iter.map(x => s"分区${index},数据: ${x._1}_${x._2}")
            }
        }).foreach(println)
    }

    def partitionsBy(rdd: RDD[(String,Int)]): Unit ={
        // value类型的partitioner是None
        // k,v类型的

//        val parRDD = rdd.partitionBy(new HashPartitioner(2))
//        val parRDD = rdd.partitionBy(new RangePartitioner(2,rdd))


        println(rdd.partitioner.get)
    }


}