package com.tunan.spark.sql.udf

import java.util

import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.mutable.ListBuffer

object ExplodeUDTF {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()

    val schema = StructType(
      StructField("teacher", StringType, true) ::
        StructField("sources", StringType, true) :: Nil
    )

    val list = new util.ArrayList[Row]()
    list.add(Row("tunan", "hive,spark,flink"))
    list.add(Row("xiaoqi", "cdh,kafka,hbase"))


    val df: DataFrame = spark.createDataFrame(list, schema)


    val line = new ListBuffer[(String, String)]()
    import spark.implicits._
    df.flatMap(x => {
      val sources = x.getString(1).split(",")
      for (source <- sources){
        line.append((x.getString(0),source))
      }
      line
    }).toDF("teacher","source").show()
  }
}


