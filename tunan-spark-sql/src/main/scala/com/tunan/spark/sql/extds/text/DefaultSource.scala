package com.tunan.spark.sql.extds.text

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider}

class DefaultSource  extends RelationProvider{
  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    val path = parameters.get("path")
    path match {
      case Some(p) =>new TextDataSourceRelation(sqlContext,p)
      case _ => throw new IllegalArgumentException("path is required ...")
    }
  }
}
