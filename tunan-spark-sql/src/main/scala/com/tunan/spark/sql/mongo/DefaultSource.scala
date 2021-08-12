package com.tunan.spark.sql.mongo

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider}

class DefaultSource extends RelationProvider {
    override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {

        val fields = parameters.get("fields")
        val start = parameters.get("start")
        if (fields.isEmpty) {
            throw new IllegalArgumentException("缺少查询的字段")
        }

        if (start.isEmpty) {
            throw new IllegalArgumentException("缺少开始的时间")
        }


        val end = parameters.getOrElse("end", start.get.toLong + 86400000L).toString.toLong

        new MongoDataSourceRelation(sqlContext, start.get.toLong, end, fields.get)

    }
}