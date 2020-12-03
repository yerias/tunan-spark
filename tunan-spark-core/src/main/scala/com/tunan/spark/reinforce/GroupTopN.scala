package com.tunan.spark.reinforce

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

object GroupTopN {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val file = sc.textFile("file:///root/files/test/url.txt")

        val mapRDD = file.map(lines => {
            val words = lines.split("\t")
            ((words(0), words(1)), 1)
        })
        mapRDD.cache()

        val ord = new Ordering[((String, String), Int)]() {
            override def compare(x: ((String, String), Int), y: ((String, String), Int)): Int = {
                if(!x._1._1.equals(y._1._1) && x._2.equals(y._2)){
                    return 1
                }
                 -(y._2 - x._2)
            }
        }

        val domains: Array[String] = mapRDD.map(x => x._1._1).distinct().collect()
        val reduceByKeyRDD = mapRDD.reduceByKey(new DomainPartitioner(domains), _ + _)
        val sortByOrdering: RDD[((String, String), Int)] = reduceByKeyRDD.mapPartitions({ partition => {
            val set = mutable.TreeSet.empty(ord)
            partition.foreach(row => {
                set.add(row)
                if (set.size > 2) {
                    set.remove(set.last)
                }
            })
            set.toIterator
        }})


        sortByOrdering.foreach(println)


        mapRDD.unpersist()

        sc.stop()

    }
}


class DomainPartitioner(domains:Array[String]) extends Partitioner{
    private val map = mutable.Map[String,Int]()
    for (i <- domains.indices){
        map(domains(i)) = i
    }

    override def numPartitions: Int = domains.length

    override def getPartition(key: Any): Int = {
        val k = key.asInstanceOf[(String, String)]
        map(k._1)
    }
}
