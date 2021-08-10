package com.tunan.spark.sql.mongo

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider}

class DefaultSource extends RelationProvider{
    override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {

        val start = parameters.get("start").get.toLong
        val end = parameters.getOrElse("end", start + 86400000L).toString.toLong

        new MongoDataSourceRelation(sqlContext,start,end)
    }
}