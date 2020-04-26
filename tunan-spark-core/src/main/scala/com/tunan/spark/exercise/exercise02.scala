package com.tunan.spark.exercise

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD

/**
 * 用户     节目               展示  点击
 * 001,一起看|电视剧|军旅|亮剑,1,1
 * 001,一起看|电视剧|军旅|亮剑,1,0
 * 002,一起看|电视剧|军旅|士兵突击,1,1
 * ==>
 * 001,一起看,2,1
 * 001,电视剧,2,1
 * 001,军旅,2,1
 * 001,亮剑,2,1
 **/
object exercise02 {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val linesRDD: RDD[String] = sc.textFile("tunan-spark-core/data/test2.txt")

        //使用map返回的是一个数组，我不要数组，就使用flatMap
        import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD
        val map2RDD: RDD[((String, String), (Int, Int))] = linesRDD.flatMap(line => {
            val words: Array[String] = line.split(",")
            val programs: Array[String] = words(1).split("\\|")
            val mapRDD: Array[((String, String), (Int, Int))] = programs.map(program => ((words(0), program), (words(2).toInt, words(3).toInt)))
            mapRDD
        })
        val groupRDD: RDD[((String, String), Iterable[(Int, Int)])] = map2RDD.groupByKey()

        //这里是mapValues很好的一个使用案例
        val mapVRDD: RDD[((String, String), (Int, Int))] = groupRDD.mapValues(x => {
            val imps: Int = x.map(_._1).sum
            val check: Int = x.map(_._2).sum
            (imps, check)
        })

        mapVRDD.map(x => {
            (x._1._1,x._1._2,x._2._1,x._2._1)
        }).print()

        sc.stop()
    }
}
