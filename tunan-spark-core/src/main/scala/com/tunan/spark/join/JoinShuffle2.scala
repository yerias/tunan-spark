package com.tunan.spark.join

import com.tunan.spark.utils.ContextUtils

object JoinShuffle2 {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(JoinShuffle.getClass.getSimpleName)

        /**
         * rdd1、rdd2任一分区数不同，会经过shuffle
         */
        val rdd1 = sc.parallelize(List(("香蕉",20), ("苹果",50), ("菠萝",30), ("猕猴桃", 50)),2)
        val rdd2 = sc.parallelize(List(("草莓",90), ("苹果",25), ("菠萝",25), ("猕猴桃", 30), ("西瓜", 45)),3)

        val rdd3 = rdd1.reduceByKey(_ + _)
        val rdd4 = rdd2.reduceByKey(_ + _)

        val joinRDD = rdd3.join(rdd4,2)
        joinRDD.collect()


        /**
         * 当分区数都一致时相同的key必然在相同的分区号上
         * 那么当两个需要连接的RDD和后面的一个Join RDD的分区都不一致时就可能导致shuffle产生
         * 分区数相同的不发生shuffle，分区数不同的发生shuffle
         *
         *
         * 0.有一种情况当两个RDD和Jion时的RDD分区数一致时,不会发生shuffle；
         * 1.当join指定的RDD和其中的一个RDD分区相同时,不同那个的RDD将可能会发生shuffle,(排出极端情况如上：一个RDD分区为2,一个RDD分区为4的情况 join时指定分区数和其中一个样 2情况)；
         * 2.当三个RDD都不相同时必然需要重新洗牌shuffle；
         */
        println("=====================")
        println(s"RDD1的分区数：${rdd1.getNumPartitions}")
        println(s"RDD2的分区数：${rdd2.getNumPartitions}")
        println(s"joinRDD的分区数：${joinRDD.getNumPartitions}")
        println("=====================")


        sc.stop()
    }
}
