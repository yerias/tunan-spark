package com.tunan.spark.topn

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


object GroupTopN_TreeSet {
    def main(args: Array[String]): Unit = {
        val in = "tunan-spark-core/data/site.log"
        //连接SparkMaster
        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val fileRDD = sc.textFile(in)

        val mapRDD = fileRDD.map(lines => {
            val words = lines.split("\t")
            ((words(0), words(1)), 1)
        })

        val domains = mapRDD.map(x => x._1._1).distinct().collect()

        val ord: Ordering[((String, String), Int)] = new Ordering[((String, String), Int)]() {
            override def compare(x: ((String, String), Int), y: ((String, String), Int)): Int = {
                if (!x._1.equals(y._1) && x._2 == y._2) {
                    return 1
                }
                    //  降序排
                y._2 - x._2
            }
        }

        val treeSort = mapRDD.reduceByKey(new MyPartitioner(domains), _ + _).mapPartitions(partition => {
            val set = mutable.TreeSet.empty(ord)
            partition.foreach(x => {
                set.add(x)
                if (set.size > 2) {
                    set.remove(set.lastKey) //移除最后一个
                }
            })
            set.toIterator
        }).collect()
        treeSort.foreach(println)

    }
}