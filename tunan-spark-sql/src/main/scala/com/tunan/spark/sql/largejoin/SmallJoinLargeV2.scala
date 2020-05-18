package com.tunan.spark.sql.largejoin

import com.tunan.spark.utils.hadoop.CheckHDFSOutPath
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object SmallJoinLargeV2 extends Logging{

  def main(args: Array[String]): Unit = {

    val start = System.currentTimeMillis()

    val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()
    val sc = spark.sparkContext

    val out = "tunan-spark-sql/moke-out"
    CheckHDFSOutPath.ifExistsDeletePath(sc.hadoopConfiguration,out)
//    val productRDD = sc.textFile("tunan-spark-sql/moke1/product_category.txt")
//    val userRDD = sc.textFile("tunan-spark-sql/moke1/user_click.txt")

        val productRDD = sc.textFile("/data/large_join/product_category2.txt")
        val userRDD = sc.textFile("/data/large_join/user_click.txt")

    val broadMap = sc.broadcast(productRDD.map(row => {
      val words = row.split(",")
      (words(0).toLong, words(1).toLong)
    }).collectAsMap())



    val userRepartitionRDD = userRDD.repartition(10)

    val mapPartition = userRepartitionRDD.mapPartitions(partition => {
      partition.map(row => {
        val words = row.split(",")
        (words(0).toLong, broadMap.value(words(1).toLong))
      })
    })

    import spark.implicits._
    val sql = mapPartition.toDF("userId", "categoryId")

    sql.show()
    sql.explain()
    sql.queryExecution.debug.codegen()

//    productMapRDD.saveAsTextFile(out)

    // 分区内有序，全局也有序
//    implicit val order: Ordering[(Long, Long)] = Ordering[(Long)].on[(Long, Long)](x => x._1)

    //排序
//    val userSortBy = userMapRDD.sortBy(x => x)
//    val productSortBy = productMapRDD.sortBy(x => x)

//    joinRDD.map( x=> s"${x._2._1},${x._2._2}").take(10)//.saveAsTextFile(out)

    val end = System.currentTimeMillis()
    log.error("一共使用时间："+(end-start)+"ms======================================================")
  }
}
