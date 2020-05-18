package com.tunan.spark.sql.readcompress

import com.hadoop.compression.lzo.LzopCodec
import com.hadoop.mapreduce.LzoTextInputFormat
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.spark.{SparkConf, SparkContext}

object ReadCompressWriteDHFS {

    var in = "/data/lzo-index/access.txt.lzo"
    var out = ""
    var flat:Boolean = true

    def main(args: Array[String]): Unit = {

        val conf: SparkConf = new SparkConf()//.setMaster("local[*]").setAppName(this.getClass.getSimpleName)
        val sc = new SparkContext(conf)

        val rdd = sc.newAPIHadoopFile(in, classOf[LzoTextInputFormat], classOf[LongWritable],
            classOf[Text]).map(x => x._2.toString)



        if (args.length>1){
            in = args(0)
            out = args(1)
            flat = args(2).toBoolean
        }

//        val rdd = sc.textFile(in)

        rdd.take(10).foreach(println)

        if (out != ""){
            if (flat){
                rdd.saveAsTextFile(out,classOf[LzopCodec])
            }else{
                rdd.saveAsTextFile(out)
            }
        }
    }
}
