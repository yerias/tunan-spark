package com.tunan.spark.exercise

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD

/**
 * 输入的数据:
 * a,1,3
 * a,2,3
 * b,1,1
 * 需要的结果：
 * a,3,6
 * b,1,1
 */

object exercise01 {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val lines: RDD[String] = sc.textFile("tunan-spark-core/data/test1.txt")

        val kvRDD: RDD[(String, (Int, Int))] = lines.map(line => {
            val words: Array[String] = line.split(",")
            (words(0), (words(1).toInt, words(2).toInt))
        })

        val reduceRDD: RDD[(String, (Int, Int))] = kvRDD.reduceByKey((x, y) => {
            (x._1 + y._1, x._2 + y._2)
        })
        import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD
        reduceRDD.map(x =>{
            (x._1,x._2._1,x._2._2)
        }).print()
    }
}
