package com.tunan.spark.sql.extds.hbase

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider}

class DefaultSource extends RelationProvider{
    // 创建Relation
    override def createRelation(sqlContext: SQLContext, p: Map[String, String]): BaseRelation = {

        // 做一些检查性的工作，然后调用HbaseRelation
        p match {
            case p => HbaseRelation(sqlContext,p)
            case _ => throw new IllegalArgumentException()
        }
    }
}
