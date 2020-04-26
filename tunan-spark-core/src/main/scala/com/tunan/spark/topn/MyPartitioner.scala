package com.tunan.spark.topn

import org.apache.spark.Partitioner

import scala.collection.mutable

class MyPartitioner(domains:Array[String]) extends Partitioner{

    val map = mutable.HashMap[String,Int]()
    for (i <- 0 until (domains.length)){
        map(domains(i)) = i
    }
    override def numPartitions: Int = domains.length

    override def getPartition(key: Any): Int = {
        val domain = key.asInstanceOf[(String, String)]._1
        map(domain)
    }
}
