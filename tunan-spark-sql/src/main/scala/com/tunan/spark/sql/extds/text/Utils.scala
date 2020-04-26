package com.tunan.spark.sql.extds.text

import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StringType}

object Utils {

  def caseTo(value:String,dataType: DataType): Any ={
    dataType match {
      case _:DoubleType => value.toDouble
      case _:LongType => value.toLong
      case _:StringType => value
    }
  }
}
