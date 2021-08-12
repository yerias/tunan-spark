package com.tunan.spark.sql.mongo

import com.alibaba.fastjson.{JSON, JSONObject}
import com.mongodb.casbah.{MongoClient, MongoDB}
import com.mongodb.{BasicDBObject, MongoCredential, ServerAddress}
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}

import java.util
import java.util.Date
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class MongoDataSourceRelation(context: SQLContext, start: Long, end: Long, fields: String) extends BaseRelation with TableScan with Logging {

    private val field: Array[String] = fields.split(",")

    private val fieldsToFields: ListBuffer[StructField] = ListBuffer.empty[StructField]

    for (elem <- field) {
        val kvFields = elem.split(":")
        if (kvFields.length != 2) {
            throw new IllegalArgumentException("数据格式错误")
        }

        val structField = kvFields(1).toLowerCase match {
            case "int" => StructField(kvFields(0), IntegerType)
            case "string" => StructField(kvFields(0), StringType)
        }

        fieldsToFields += structField
    }

    override def schema: StructType = StructType(fieldsToFields)


    override def buildScan(): RDD[Row] = {

        val url2 = "xxxx"
        val port = 9999
        val user = "xxxx"
        val password = "xxxx"
        val db = "xxxx"
        val document = "xxxx"

        val server = new ServerAddress(url2, port)
        val credentials = MongoCredential.createCredential(user, db, password.toCharArray)
        val mongoClient = MongoClient(server, List(credentials))
        val mongoDatabase: MongoDB = mongoClient.getDB(db)
        val collection = mongoDatabase.getCollection(document)


        // 时间过滤
        val startTime = new BasicDBObject("UpdateDate", new BasicDBObject("$gte", new Date(start)))
        val endTime = new BasicDBObject("UpdateDate", new BasicDBObject("$lt", new Date(end)))
        val query = new BasicDBObject("$and", util.Arrays.asList(startTime, endTime))
        val findInterable = collection.find(query)

        val mongoCursor = findInterable.iterator()

        val schemaField: Array[StructField] = schema.fields

        var seqs = mutable.ListBuffer[String]()
        val filed = mutable.ArrayBuffer[String]()
        while (mongoCursor.hasNext) {
            val str = mongoCursor.next().toString
            var json: JSONObject = null
            try {
                json = JSON.parseObject(str)
                // ClassId:string,ProductId:string,Mode:string,ObjectId:string,Status:string,Client:string,UpdateDate:string
                filed.clear()
                for (elem <- field) {
                    val kvFields = elem.split(":")
                    if (kvFields.length != 2) {
                        throw new IllegalArgumentException("数据格式错误")
                    }

                    if(kvFields(0) != "UpdateDate"){
                        filed += json.getString(kvFields(0))
                    }else{
                        filed += json.getJSONObject("UpdateDate").getOrDefault("$date", "").toString
                    }
                }

                seqs.+=(filed.mkString(","))
                filed.clear()
            } catch {
                case ex: Exception => println("产生的异常: " + ex.printStackTrace())
            }
        }

        val rdd = context.sparkSession.sparkContext.parallelize(seqs)
        rdd.map(_.split(",").map(_.trim)).map(_.zipWithIndex.map {
            case (value, index) => {
                ToolUtils.caseTo(value, schemaField(index).dataType)
            }
        }).map(x => Row.fromSeq(x))
    }

    override def sqlContext: SQLContext = ???
}
