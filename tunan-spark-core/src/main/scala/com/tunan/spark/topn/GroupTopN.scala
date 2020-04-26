package com.tunan.spark.topn

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


object GroupTopN {
    def main(args: Array[String]): Unit = {

        val in = "tunan-spark-core/data/site.log"
        //连接SparkMaster
        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        //从HDFS中读取输入文件并创建RDD
        val lines = sc.textFile(in)
        //        lines.foreach(println) //输出观察


        val broadcastTopN = sc.broadcast[Integer](2) //指定TopN的N值
        val broadcastDirection = sc.broadcast("top") //指定取最大的还是最小的，top/bottom

        val pairs = lines.map { line =>
            val words = line.split("\t")
            ((words(0), words(1)), 1)
        }
        val domains = pairs.map(x => x._1._1).distinct().collect()

        val reduceRDD = pairs.reduceByKey(new MyPartitioner(domains), _ + _)

        val MyOrd: Ordering[((String, String), Int)] = new Ordering[((String, String), Int)] {
            override def compare(x: ((String, String), Int), y: ((String, String), Int)): Int = {
                /**
                 * k可能相同可能不同，
                 * 1. 如果k不相同，v相同，返回1，升序排序
                 * 2. 如果k相同，v相同 k取反 判断y._2-x._2
                 * 3. 如果k不相同 v不相同 判断y._2-x._2
                 * 4. 如果k相同 v不相同 判断y._2-x._2
                 * v必须相同，也就是比较的值必须相同
                 */
                if (!x._1.equals(y._1) && x._2 - y._2 == 0)
                    return 1
                // 这个值决定是升序还是降序
                y._2 - x._2
            }
        }

        val partRDD = reduceRDD.mapPartitions(partition => {

            val set = mutable.TreeSet.empty(MyOrd)
            partition.foreach(x => {
                set.add(x)
                if (set.size > 2) {
                    set.remove(set.last)
                }
            })
            set.toIterator
        }).collect
        partRDD.foreach(println)
    }
}