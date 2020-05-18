package com.tunan.spark.sql.window

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

object SkewWindow {

    def main(args: Array[String]): Unit = {
            val spark = SparkSession
                  .builder()
                  .master("local[2]")
                  .appName(this.getClass.getSimpleName)
                  .getOrCreate()

        val sc = spark.sparkContext

        // 倾斜： 核心思想 => 分组再处理
        val in = "tunan-spark-sql/data/skew.txt"
        val textRDD = sc.textFile(in)

        // 抽样查看倾斜值
        val sampleRDD: RDD[String] = textRDD.sample(withReplacement = false, 0.1)

//        sampleRDD.foreach(println)

        val random = new Random()


        val isBaidu = textRDD.filter(_.equals("www.baidu.com")).map(x => {
            val r = random.nextInt(5)
            x + "|" + r
        })
        import spark.implicits._
        val process: RDD[(String, Int)] = isBaidu.map((_, 1)).reduceByKey(_ + _).map(x => (x._1.split("\\|")(0),x._2))

        val notBaidu = textRDD.filter(!_.equals("www.baidu.com")).map((_,1)).reduceByKey(_+_)

        val unionRDD = process.union(notBaidu)

        val domainRDD: RDD[Domain] = unionRDD.reduceByKey(_ + _).map(x => {
            Domain(x._1, x._2.toInt)
        })

        val df = domainRDD.toDF
        df.printSchema()

        import org.apache.spark.sql.expressions.Window
        import org.apache.spark.sql.functions._

        // 1. 分组 排序
        val windowSpec = Window.orderBy('cnt.desc)
//        val windowSpec = Window.partitionBy('domain).orderBy('cnt.desc)
        // 2. 开窗
        val win: DataFrame = df.withColumn("rank", row_number().over(windowSpec))
        // 3. 过滤
        val filter =win.where('rank <= 3)

        filter.show()


//        val windowSpec = Window.partitionBy('domain).orderBy('cnt)
//        val win = domainDF.withColumn("r", row_number().over(windowSpec))
//
//        val result = win.where('r <= 3)
//        result.show()
    }

    case class Domain(domain:String,cnt:Int)
}
