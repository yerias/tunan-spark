package com.tunan.spark.accumulator

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel

/**
 * 需求：id后三位相同的加入计数器
 */
object MyCollectionAccumulator {

    def main(args: Array[String]): Unit = {
        val sc  = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val acc = sc.collectionAccumulator[People]("集合计数器")

        val rdd= sc.parallelize(Array(People("tunan", 100000), People("xiaoqi", 100001), People("张三", 100222), People("李四", 100003)))

        rdd.map(x => {
            val id2 = x.id.toString.reverse
            if (id2(0) == id2(1) && id2(0) ==id2(2)){
                acc.add(x)
            }
        }).persist(StorageLevel.MEMORY_ONLY_SER).count()



        println(acc.value)
        sc.stop()
    }
    case class People(name:String,id:Long);
}
