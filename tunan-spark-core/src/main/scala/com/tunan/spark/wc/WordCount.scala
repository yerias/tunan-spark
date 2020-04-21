package com.tunan.spark.wc

import com.tunan.hadoop.CheckHDFSOutPath
import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

    def main(args: Array[String]): Unit = {

        //输入、输出路径
//        val in = "tunan-spark-core/data/wc.txt"
//        val out = "tunan-spark-core/out"
        val (in,out) = (args(0),args(1))

        //配置conf
        val conf = new SparkConf()
            .setAppName(getClass.getSimpleName)
            .setMaster("local[2]")
            //监听类注册
            .set("spark.extraListeners", "com.tunan.spark.listener.MySparkListenerV2")
            .set("spark.send.mail.enabled","true")


        //拿到sc
        val sc = new SparkContext(conf)


        //删除输出目录
        CheckHDFSOutPath.ifExistsDeletePath(new Configuration(),out)

        //操作算子
        val result = sc.textFile(in).flatMap(_.split("\t")).map((_, 1)).reduceByKey(_ + _)

        //保存文件
        result.saveAsTextFile(out)

        sc.stop()
    }
}
