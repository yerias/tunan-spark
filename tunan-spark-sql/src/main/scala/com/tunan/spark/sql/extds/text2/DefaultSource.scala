package com.tunan.spark.sql.extds.text2

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider}

class DefaultSource extends RelationProvider{
    override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
        // 从parameters拿到path
        val p = parameters.get("path")
        p match{
                // 匹配成功
            case Some(p) => new TextDataSourceRelation(sqlContext,p)
                // 匹配失败
            case _ => throw new IllegalArgumentException("path参数异常，不能创建textDataSourceRelation...")
        }

    }
}
