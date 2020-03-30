package com.tunan.spark.job

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD
import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD

object BaseStation {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(BaseStation.getClass.getSimpleName)

        //读取文件
        val lines: RDD[String] = sc.textFile("tunan-spark-core/data/station")
        val mapRDD = lines.map(line => {
            val words: Array[String] = line.split("\t")
            //使用对象接收
            BaseLog(words(0), words(1), words(2), words(3), words(4), words(5), words(6), words(7))
        })

        //转换为kv结构
        val map2RDD = mapRDD.map(x => ((x.product_no, x.lac_id), (x.moment, x.start_time, x.user_id, x.county_id, x.staytime, x.city_id)))

        //reduceByKey聚合
        val reduceRDD: RDD[((String, String), (String, String, String, String, String, String))] = map2RDD.reduceByKey((x, y) => {
            (x._1, x._2, x._3, x._4, (x._5.toLong + y._5.toLong).toString, x._6)
        })

        //集合后的结果转换为对象，方便排序
        val map3RDD: RDD[BaseLog] = reduceRDD.map(x => {
            //鬼知道这一长串是啥
            BaseLog(x._1._1, x._1._2, x._2._1, x._2._2, x._2._3, x._2._4, x._2._5, x._2._6)
        })
        //排序
        implicit val order: Ordering[BaseLog] = Ordering[(Long, Long)].on[BaseLog](x => (x.product_no.toLong, x.staytime.toLong))

        //输出结果
        map3RDD.sortBy(x => x).print()
    }
    // 使用case class 格式化输出
    case class BaseLog(product_no:String, lac_id:String, moment:String, start_time:String, user_id:String, county_id:String, staytime:String, city_id:String){
        override def toString: String = s"$product_no $lac_id $moment $start_time $user_id $county_id $staytime $city_id"
    }
}

