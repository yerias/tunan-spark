package com.tunan.spark.transformation

import com.tunan.spark.utils.ContextUtils
import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD
import org.apache.spark.rdd.RDD

object JoinAndCogroup {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(JoinAndCogroup.getClass.getSimpleName)

        val list1RDD = sc.parallelize(List((1, "华山派"), (2, "武当派"), (3, "明教"), (4, "崆峒派")))
        val list2RDD = sc.parallelize(List((1, 66), (2, 77), (3, 88)))

        /**
         * join（otherDataSet，[numTasks]）
         * 加入一个RDD，在一个（k，v）和（k，w）类型的dataSet上调用，返回一个（k，（v，w））的pair dataSet。
         */
        val joinRDD: RDD[(Int, (String, Int))] = list1RDD.join(list2RDD)
        joinRDD.print()
        println("------------------------------------------")
        /**
         * 合并两个RDD，生成一个新的RDD。实例中包含两个Iterable值，第一个表示RDD1中相同值，第二个表示RDD2
         * 中相同值（key值），这个操作需要通过partitioner进行重新分区，因此需要执行一次shuffle操作。（
         * 若两个RDD在此之前进行过shuffle，则不需要）
         */
        val cogroupRDD: RDD[(Int, (Iterable[String], Iterable[Int]))] = list1RDD.cogroup(list2RDD)
        cogroupRDD.print()
        println("------------------------------------------")


        val flatMapRDD: RDD[(Int, (String, Int))] = cogroupRDD.flatMapValues(pair => {
            for (v <- pair._1.iterator; w <- pair._2.iterator) yield (v, w)
        })
        flatMapRDD.print()
        println("------------------------------------------")

        //TODO join就是把两个集合根据key,进行内容聚合，而cogroup只会对相同的key进行合并。
    }
}
