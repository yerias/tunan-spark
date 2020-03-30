package com.tunan.spark.job

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD
import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD

object BaseStationV2 {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(BaseStationV2.getClass.getSimpleName)

        //读取文件
        val lines: RDD[String] = sc.textFile("tunan-spark-core/data/station")
        val mapRDD = lines.map(line => {
            val words: Array[String] = line.split("\t")
            //使用tuple接收
            ((words(0), words(1)), (words(2), words(3), words(4), words(5), words(6), words(7)))
        })

        //使用reduceByKey集合,注意这里的写法
        val reduceRDD = mapRDD.reduceByKey((x, y) => {
            (x._1, x._2, x._3, x._4, (x._5.toLong + y._5.toLong).toString, x._6)
        })

        //使用map将kv结构转换为tuple
        val map2RDD= reduceRDD.map(x => {
            (x._1._1, x._1._2, x._2._1, x._2._2, x._2._3, x._2._4, x._2._5, x._2._6)
        })

        //传入tuple，排序
        implicit val order = Ordering[(Long)].on[(String, String, String, String, String, String, String, String)](x => x._1.toLong)

        //输出结果
        map2RDD.sortBy(x=>x).print()
    }
}




