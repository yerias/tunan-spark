package com.tunan.spark.sql.largejoin

import com.tunan.spark.utils.hadoop.CheckHDFSOutPath
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SmallJoinLarge extends Logging{

  def main(args: Array[String]): Unit = {

    val start = System.currentTimeMillis()

    val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(conf)

    val out = "tunan-spark-sql/moke-out"
    CheckHDFSOutPath.ifExistsDeletePath(sc.hadoopConfiguration,out)


    val userRDD = sc.textFile("tunan-spark-sql/moke2/user_click.txt")
    val productRDD = sc.textFile("tunan-spark-sql/moke2/product_category.txt")

    val userRepartitionRDD = userRDD.repartition(10)
    val productRepartitionRDD = productRDD.repartition(10)

    val userMapRDD = userRepartitionRDD.mapPartitions(partition => {
      partition.map(row => {
        val words = row.split(",")
        (words(1).toLong, words(0).toLong)
      })
    })

    val productMapRDD = productRepartitionRDD.mapPartitions(partition => {
      partition.map(row => {
        val words = row.split(",")
        (words(0).toLong, words(1).toLong)
      })
    })
//    productMapRDD.saveAsTextFile(out)

    // 分区内有序，全局也有序
    implicit val order: Ordering[(Long, Long)] = Ordering[(Long)].on[(Long, Long)](x => x._1)

    //排序
//    val userSortBy = userMapRDD.sortBy(x => x)
//    val productSortBy = productMapRDD.sortBy(x => x)

    val joinRDD = userMapRDD.join(productMapRDD)

    joinRDD.sortBy(x => x).map( x=> s"${x._2._1},${x._2._2}").take(10)//.saveAsTextFile(out)

    val end = System.currentTimeMillis()
    log.error("一共使用时间："+(end-start)+"ms======================================================")
  }
}
