package com.tunan.spark.multipledirectory

import com.tunan.spark.utils.ContextUtils
import com.tunan.spark.utils.hadoop.CheckHDFSOutPath
import org.apache.spark.rdd.RDD

object MultipleDirectory {
    def main(args: Array[String]): Unit = {
        val out = "tunan-spark-core/out"
        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)
        CheckHDFSOutPath.ifExistsDeletePath(sc.hadoopConfiguration,out)
        //读取数组，转换成键值对的格式
        val lines = sc.textFile("tunan-spark-core/ip/access-result/*")
        val mapRDD: RDD[(String, String)] = lines.map(line => {
            val words = line.split(",")
            (words(12), line)
        })
        //多目录保存文件
        mapRDD.saveAsHadoopFile(out,classOf[String],classOf[String],classOf[MyMultipleTextOutputFormat])
        sc.stop()
    }
}
