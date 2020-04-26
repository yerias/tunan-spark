package com.tunan.spark.sql.udf

import java.util

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DataType, DoubleType, IntegerType, LongType, StringType, StructField, StructType}

object AvgUDAF {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()


    val schema = StructType(
      StructField("name", StringType, true)::
      StructField("age", IntegerType, true)::
      StructField("sex", StringType, true)::Nil
    )

    val list = new util.ArrayList[Row]()
    list.add(Row("zhangsan",18,"男"))
    list.add(Row("lisi",20,"男"))
    list.add(Row("wangwu",26,"女"))
    list.add(Row("翠翠",18,"女"))
    list.add(Row("闰土",8,"男"))

    val df = spark.createDataFrame(list, schema)

    df.createOrReplaceTempView("people")

    spark.udf.register("age_avg_udaf",AgeAvgUDAF)

    spark.sql("select sex,age_avg_udaf(age) as ave_age from people group by sex").show()

  }

  object AgeAvgUDAF extends UserDefinedAggregateFunction{
    //输入类型
    override def inputSchema: StructType = StructType(
      StructField("input",DoubleType,true)::Nil
    )
    //聚合内部中的buffer类型
    override def bufferSchema: StructType = StructType(
      StructField("sums",DoubleType,true)::
      StructField("num",LongType,true)::Nil
    )

    //输入数据类型
    override def dataType: DataType = DoubleType

    //输入数据类型是否和输出数据类型相等
    override def deterministic: Boolean = true

    //聚合内部buffer的初始化
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0.0
      buffer(1) = 0L
    }

    //分区内更新聚合buffer
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      buffer.update(0,buffer.getDouble(0)+input.getDouble(0))
      buffer.update(1,buffer.getLong(1)+1)
    }

    //分区间合并
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1.update(0,buffer1.getDouble(0)+buffer2.getDouble(0))
      buffer1.update(1,buffer1.getLong(1)+buffer2.getLong(1))
    }

    //最终计算
    override def evaluate(buffer: Row): Any = {
      buffer.getDouble(0)/buffer.getLong(1)
    }
  }
}
