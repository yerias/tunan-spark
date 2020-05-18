package com.tunan.spark.sql.extds.text2

import com.hadoop.mapreduce.LzoTextInputFormat
import com.tunan.spark.sql.extds.text.Utils
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}

class TextDataSourceRelationWithLzoCompress(context: SQLContext, path: String) extends BaseRelation with TableScan with Logging {
    override def sqlContext: SQLContext = context

    override def schema: StructType = StructType(
        List(
            StructField("year", StringType),
            StructField("month", StringType),
            StructField("day", StringType),
            StructField("country", StringType),
            StructField("province", StringType),
            StructField("city", StringType),
            StructField("area", StringType),
            StructField("proxyIp", StringType),
            StructField("requestTime", LongType),
            StructField("referer", StringType),
            StructField("method", StringType),
            StructField("http", StringType),
            StructField("domain", StringType),
            StructField("path", StringType),
            StructField("httpCode", StringType),
            StructField("requestSize", LongType),
            StructField("responseSize", LongType),
            StructField("cache", StringType),
            StructField("userId", LongType)
        )
    )

    override def buildScan(): RDD[Row] = {
        logError("进入buildScan方法")
        // 使用RDD拿到Lzo本文内容

        val lines = sqlContext.sparkContext.newAPIHadoopFile(path, classOf[LzoTextInputFormat], classOf[LongWritable], classOf[Text])
          .map(_._2.toString)
        // 拿到响应的schema信息
        val fields = schema.fields
        // 拿到每行数据，做简单处理，返回RDD[Row]
        lines.map(_.split(",").map(_.trim)).map(_.zipWithIndex.map {
            case (value, index) =>
                Utils.caseTo(value, fields(index).dataType)
        }).map(x => Row.fromSeq(x))
    }
}
