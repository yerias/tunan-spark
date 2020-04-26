package com.tunan.spark.sql.largejoin

import org.apache.spark.Partitioner

import scala.collection.mutable

class MyPartitioner(categorys: Array[Long]) extends Partitioner{

  val map = mutable.HashMap[Long, Int]()
  for (i <- categorys.indices){
     map(categorys(i))= i
  }

  override def numPartitions: Int = categorys.length

  override def getPartition(key: Any): Int = {
    // 拿到key里的第一个元素作为分区元素
    val category = key.asInstanceOf[(Long, Long)]._1
    map(category)
  }
}
