package com.tunan.spark.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Transformation02 {

    def main(args: Array[String]): Unit = {

        val text = List(("C",10),("C",2),("B",4),("A",5),("A",2),("B",3))
        val seq = List(1,2,3,4,5,6,7)    // 7+3+
        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val rdd: RDD[(String,Int)] = sc.parallelize(text,3)
        val seqRDD = sc.parallelize(seq,3)

        // groupByKey
        // reduceByKey
        // aggregateByKey
        // combineByKey
        // foldByKey
        // combineByKeyWithClassTag


//        groupByKey(rdd)
//        reduceByKey(rdd)
//        aggregateByKey(rdd)
//        aggregate(seqRDD)
//        combineByKey(rdd)
//        combineByKeyToAvg(rdd)
//        foldByKey(rdd)
        combineByKeyWithClassTag(rdd)

    }


    //  老祖宗
    private def combineByKeyWithClassTag(rdd: RDD[(String, Int)]): Unit = {
        rdd.combineByKeyWithClassTag(
            v => v+1,               // 初始函数
            (a:Int,b:Int) => a + b, // 分区内计算
            (a:Int,b:Int) => a + b  // 分区间计算
        ).foreach(println)
    }

    // combineByKey实现平均数
    private def combineByKeyToAvg(rdd: RDD[(String, Int)]): Unit = {
        rdd.combineByKey(
            v => (v,1),     // 初始函数
            (a:(Int,Int),b:Int) => (a._1+b,a._2+1), // 分区内计算
            (a:(Int,Int),b:(Int,Int)) => (a._1+b._1,a._2+b._2)  // 分区间计算
        ).map( x => (x._1,x._2._1/x._2._2.toDouble)).foreach(println)
    }

    // 底层调用combineByKeyWithClassTag，直接传入初始函数，func1，func2
    private def combineByKey(rdd: RDD[(String, Int)]): Unit = {
        rdd.combineByKey(
            v => v+1,               // 初始函数
            (a:Int,b:Int) => a + b, // 分区内计算
            (a:Int,b:Int) => a + b  // 分区间计算
        ).foreach(println)
        //  combineByKeyWithClassTag(createCombiner, mergeValue, mergeCombiners)(null)
    }

    // 初始值分区内作用于Key，分区间不作用 => 与aggregate不同点
    // 底层调用combineByKeyWithClassTag，可传入func1和func2，且初始值和v作用于func1
    // 相比较于reduceByKey多了初始值的和两个func的选择。
    private def aggregateByKey(rdd: RDD[(String, Int)]): Unit = {
        rdd.aggregateByKey(2)(_+_,_*_).foreach(println)
        // combineByKeyWithClassTag[U]((v: V) => cleanedSeqOp(createZero(), v),cleanedSeqOp, combOp, partitioner)

    }

    // 底层调用combineByKeyWithClassTag，跟reduceByKey比较多了初始值，和aggregateByKey比较只有一个func
    // 分区内作用于Key，分区间不作用
    private def foldByKey(rdd: RDD[(String, Int)]): Unit = {
        rdd.foldByKey(1)(_+_).foreach(println)
        // combineByKeyWithClassTag[V]((v: V) => cleanedFunc(createZero(), v),cleanedFunc, cleanedFunc, partitioner)
    }

    // 底层调用combineByKeyWithClassTag，初始函数v=>v，两个函数都是func(_+_)
    private def reduceByKey(rdd: RDD[(String, Int)]): Unit = {
        rdd.reduceByKey(_+_).foreach(println)
        // fund => _+_  combineByKeyWithClassTag[V]((v: V) => v, func, func, partitioner)
    }

    private def groupByKey(rdd: RDD[(String, Int)]): Unit = {
        rdd.groupByKey().mapValues(_.sum).foreach(println)
        // combineByKeyWithClassTag[CompactBuffer[V]](createCombiner, mergeValue, mergeCombiners, partitioner, mapSideCombine = false)

    }

    // 作用于普通rdd，初始值既针对分区内也针对分区间，每个分区计算一次，分区间计算一次 => n+1
    private def aggregate(rdd: RDD[Int]): Unit = {
        val res = rdd.aggregate(1)(_ + _, _ + _)
        println(res)

    }
}
