package com.tunan.spark.sql.extds.text

import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}

class TextDataSourceRelation(context:SQLContext,path:String) extends BaseRelation with TableScan with Logging{

  override def sqlContext: SQLContext = context

  override def schema: StructType = StructType{
    List(
      StructField("id",StringType,true),
      StructField("name",StringType,true),
      StructField("sex",StringType,true),
      StructField("sal",DoubleType,true),
      StructField("comm",DoubleType,true)
    )
  }

  override def buildScan(): RDD[Row] = {
    logError("进入buildScan方法")
    val textRDD = sqlContext.sparkContext.textFile(path)
    val schemaField: Array[StructField] = schema.fields
    textRDD.map(_.split(",").map(_.trim)).map(row => row.zipWithIndex.map {
        case (value, index) =>
          val columnName = schemaField(index).name
          Utils.caseTo(if (columnName.equalsIgnoreCase("sex")) {
            if (value == "1") {
              "男"
            } else if (value == "2") {
              "女"
            } else {
              "未知"
            }
          } else {
            value
          }, schemaField(index).dataType)
    }).map(x => Row.fromSeq(x))
  }
}

/*textRDD.map(row => {
      row.split(",").map(_.trim)).map(x => x.zipWithIndex.map{
        case(value,index) => {
          val name: String = schemaField(index).name
          Utils.caseTo(if (name.equalsIgnoreCase("sex")){
            if (value == "1"){
              "男"
            }else if(value == "2"){
              "女"
            }else{
              "未知"
            }
          }else{
            value
          },schemaField(index).dataType)
        }
    }).map(x => Row.fromSeq(x))
*/
