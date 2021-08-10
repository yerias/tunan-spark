package com.tunan.spark.sql.mongo

import java.sql.Date
import java.text.SimpleDateFormat
import java.util

import com.alibaba.fastjson.{JSON, JSONObject}
import com.mongodb.casbah.{MongoClient, MongoDB}
import com.mongodb.{BasicDBObject, MongoCredential, ServerAddress}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}

import scala.collection.mutable

class MongoDataSourceRelation(context: SQLContext, start: Long, end: Long) extends BaseRelation with TableScan with Logging {
    override def sqlContext: SQLContext = context

    override def schema: StructType = StructType(
        List(
            StructField("id", StringType),
            StructField("houseId", StringType),
            StructField("houseType", StringType),
            StructField("memberId", StringType),
            StructField("createTime", StringType),
            StructField("hsmid", StringType),
            StructField("hsid", StringType),
            StructField("area", StringType),
            StructField("roomNumber", StringType),
            StructField("price", StringType),
            StructField("shopMapName", StringType),
            StructField("status", StringType),
            StructField("hid", StringType),
            StructField("stressName", StringType),
            StructField("houseNumber", StringType)
        )
    )


    override def buildScan(): RDD[Row] = {

        val url = "mongodb.xhj.com"
        val port = 27017
        //        val url = "39.108.3.210"
        //        val port = 27018
        val db = "cms"
        val user = "readxhjDb"
        val password = "Xhjreaddb"
        val document = "cms_house_collect"

        val server = new ServerAddress(url, port)
        val credentials = MongoCredential.createCredential(user, db, password.toCharArray)
        val mongoClient = MongoClient(server, List(credentials))
        val mongoDatabase: MongoDB = mongoClient.getDB(db)
        val collection = mongoDatabase.getCollection(document)

        // TODO 时间为三个月前到昨天或者按天更新
        val startTime = new BasicDBObject("createTime", new BasicDBObject("$gte", new Date(start)))
        val endTime = new BasicDBObject("createTime", new BasicDBObject("$lt", new Date(end)))
        val query = new BasicDBObject("$and", util.Arrays.asList(startTime, endTime))
        val findInterable = collection.find(query)

        val mongoCursor = findInterable.iterator()

        val schemaField: Array[StructField] = schema.fields

        var seqs = mutable.ListBuffer[String]()

        while (mongoCursor.hasNext) {
            val bObject = mongoCursor.next()
            val str = bObject.toString
            var json: JSONObject = null
            var id = ""
            var houseId = ""
            var houseType = ""
            var memberId = ""
            var createTime = ""
            var hsmid = ""
            var hsid = ""
            var area = "0"
            var roomNumber = "0"
            var price = "0"
            var shopMapName = ""
            var status = "0"
            var hid = ""
            var stressName = ""
            var houseNumber = "0"
            try {
                json = JSON.parseObject(str)

                memberId = json.getString("memberId")
                id = json.getString("_id")
                houseType = json.getString("houseType")
                createTime = json.getJSONObject("house").getJSONObject("createTime").getString("$date")
                hsmid = json.getJSONObject("house").getString("hsmid")
                hsid = json.getJSONObject("house").getString("hsid")
                area = json.getJSONObject("house").getString("area")
                roomNumber = json.getJSONObject("house").getString("roomNumber")
                price = json.getJSONObject("house").getString("price")
                shopMapName = json.getJSONObject("house").getString("shopMapName")
                status = json.getString("status")
                hid = json.getJSONObject("house").getString("hid")
                stressName = json.getJSONObject("house").getString("stressName").split(",")(0)
                houseNumber = json.getJSONObject("house").getString("houseNumber")
                houseId = json.getOrDefault("houseId", "").toString
                createTime = json.getJSONObject("house").getJSONObject("createTime").getOrDefault("$date", "").toString
                memberId = json.getOrDefault("memberId", "").toString

                val collect: String = Collect(id, houseId, houseType, memberId, createTime, hsmid, hsid, area, roomNumber, price, shopMapName, status, hid, stressName, houseNumber).toString

                seqs.+=(collect)

            } catch {
                case ex: Exception => println("产生的异常: " + ex)
            }

        }

        val day = ToolUtils.getYesterdayDate

        val rdd = context.sparkSession.sparkContext.parallelize(seqs)
        rdd.map(_.split(",").map(_.trim)).filter(row => new SimpleDateFormat("yyyy-MM-dd").format(new Date(if (StringUtils.isNotEmpty(row(4))) row(4).toLong else 0)) <= day).map(_.zipWithIndex.map {
            case (value, index) => {

                ToolUtils.caseTo(
                    if (schemaField(index).name.equalsIgnoreCase("createTime")) {
                        val date: String = new SimpleDateFormat("yyyy-MM-dd").format(new Date(if (StringUtils.isNotEmpty(value)) value.toLong else 0))
                        date
                    } else {
                        value
                    }, schemaField(index).dataType)
            }
        }).map(x => Row.fromSeq(x))
    }
}
