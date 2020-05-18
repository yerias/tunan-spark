package com.tunan.spark.sql.readcompress

import org.apache.spark.sql.{DataFrame, SparkSession}

object ReadCompressFileWriteHive {

    var in: String = _
    var out: String = _
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
          .enableHiveSupport()
          .getOrCreate()


        if (args.length > 0) {
            in = args(0)
            out = args(1)
            flat = args(2).toBoolean
        }

        // 通过自定义外部数据源的方式读取text文本为DF
        val textDf: DataFrame = spark.read.format("com.tunan.spark.sql.readcompress").load(in)

        // TODO 若干业务操作

        if (!flat){
            // 保存到Hive
            textDf.write.format("orc").mode("overwrite").saveAsTable("store_format.parquet_tb")
        }else{
            // 保存到Hive,压缩格式为lz4
            textDf.write.format("orc").option("compression","lzo").mode("overwrite").saveAsTable("store_format.parquet_tb")
        }
    }
}
