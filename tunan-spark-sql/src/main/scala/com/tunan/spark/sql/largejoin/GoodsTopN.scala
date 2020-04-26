package com.tunan.spark.sql.largejoin

import com.tunan.spark.utils.hadoop.CheckHDFSOutPath
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object GoodsTopN {

  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(conf)

    val out = "tunan-spark-sql/out"
    CheckHDFSOutPath.ifExistsDeletePath(sc.hadoopConfiguration,out)

    val fileRDD = sc.textFile("tunan-spark-sql/moke2/product_category.txt")

    //每个类目(分组) 点击量最大(count) 的50个商品(top)

    // ((类目，商品)，1)
    val mapRDD = fileRDD.map(row => {
      val words = row.split(",")
      ((words(1).toLong, words(0).toLong), 1)
    })

    // 拿到所有类目
    val categorys = mapRDD.map(_._1._1).distinct().collect()

    // 自定义排序
    val order= new Ordering[((Long, Long), Int)]() {
      override def compare(x: ((Long, Long), Int), y: ((Long, Long), Int)): Int = {
        if (!x._1.equals(y._1) && x._2 == y._2) {
          return 1
        }
        y._2 - x._2
      }
    }

    // 分区计算
    val treeSort = mapRDD.reduceByKey( new MyPartitioner(categorys),_ + _).mapPartitions(partition => {
      val set = mutable.TreeSet.empty(order)
      partition.foreach(x => {
        set.add(x)
        if (set.size > 50) {
          set.remove(set.lastKey)
        }
      })
      set.toIterator
    })


    treeSort.map(x=>s"${x._1._1} ${x._1._2} ${x._2}").coalesce(10).saveAsTextFile(out)
    val end = System.currentTimeMillis()
    println("整个程序用了："+(end-start)+"ms")
  }
}
