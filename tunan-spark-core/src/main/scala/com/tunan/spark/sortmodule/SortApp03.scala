package com.tunan.spark.sortmodule

import com.tunan.spark.utils.ContextUtils
import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD

object SortApp03 {

    def main(args: Array[String]): Unit = {
        val sc = ContextUtils.getSparkContext(SortApp03.getClass.getSimpleName)

        //TODO List中每个东西的含义：名称name、价格price、库存amount
        val rdd = sc.parallelize(List("西瓜 20 100", "苹果 10 500", "香蕉 10 30", "菠萝 30 200"))

        //TODO 面向对象的方式实现排序
        val productRDD = rdd.map(x => {
            val split = x.split(" ")
            val name = split(0)
            val price = split(1).toDouble
            val amount = split(2).toInt
            (name, price, amount)
        })

        /**
         *(Double,Int) : 定义排序规则的返回值类型   ， 可以是类
         *(String,Double,Int) : 进来数据的类型
         * (x => (-x._2,-x._3)) : 定义排序的规则
         */
        implicit val order: Ordering[(String, Double, Int)] = Ordering[(Double,Int)].on[(String,Double,Int)](x => (-x._2,-x._3))

        //到底谁的优先级高
        productRDD.sortBy(x => x).print()
        sc.stop()
    }
}