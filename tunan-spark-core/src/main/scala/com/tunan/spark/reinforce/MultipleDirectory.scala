package com.tunan.spark.reinforce

import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat
import org.apache.spark.{SparkConf, SparkContext}

class MultipleDirectory extends MultipleTextOutputFormat[Any, Any] {
    override def generateActualKey(key: Any, value: Any): Any = NullWritable.get()

    override def generateActualValue(key: Any, value: Any): Any = value.asInstanceOf[String]

    override def generateFileNameForKeyValue(key: Any, value: Any, name: String): String = {
        s"${key}/2020-11-24"
    }
}

object MultipleDirectory{
    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
        val sc = new SparkContext(conf)

        val out = "tunan-spark-core/out"

        val files = sc.textFile("tunan-spark-core/data/site.log")
        val mapRDD = files.map(row => {
            val words = row.split("\t")
            (words(0), row)
        })

        mapRDD.saveAsHadoopFile(out,classOf[String],classOf[String],classOf[MultipleDirectory])

        sc.stop()
    }
}