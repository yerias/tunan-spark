package com.tunan.spark.sql.readcompress

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object ReadCompressFileWriteMySQL {

    var in: String = _
    var flat: Boolean = _

    def main(args: Array[String]): Unit = {

/*        val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
          .enableHiveSupport()
          .getOrCreate()*/

        val spark = SparkSession
          .builder()
          .getOrCreate()


        if (args.length > 0) {
            in = args(0)
            flat = args(1).toBoolean
        }

        // 通过自定义外部数据源的方式读取text文本为DF
        val textDf: DataFrame = spark.read.format("com.tunan.spark.sql.readcompress").load(in)

        // TODO 若干业务操作
        textDf.createOrReplaceTempView("access")

        val result = spark.sql("select domain,sum(responseSize) as sum_response_size from access group by domain order by sum_response_size desc")

        val properties = new Properties()
        properties.put("user","root")
        properties.put("password","root")

        val url = "jdbc:mysql://hadoop:3306/tunan?useUnicode=true&characterEncoding=utf-8"

        if (!flat){
            // 保存到MySQL
            result.write.mode(SaveMode.Overwrite)
              .jdbc(url,"domain_group",properties)
        }else{
            // 保存到MySQL,压缩格式为lzo
            result.write.format("orc").option("compression","lzo").mode("overwrite")
              .jdbc(url,"domain_group",properties)
        }
    }
}
