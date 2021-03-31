package com.tunan.spark.sql.extds.hbase

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory}
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat


object HBaseDataSourceUtils {

    /**
     * 读取输入的字段类型做转换
     * @param sparkTableScheme 外部传入的Schema
     * @return
     */
    def extractSparkFields(sparkTableScheme:String):Array[SparkSchema] = {
        // 除去左右括号以及按逗号切分
        val columns = sparkTableScheme.trim.drop(1).dropRight(1).split(",").map(_.trim)
        // 拿到切分后的每一对Schema

        val sparkSchemas: Array[SparkSchema] = columns.map(x => {
            val words = x.split(":").map(_.trim)
            // 使用SparkSchema封装，这里拿什么封装无所谓，tuple都行
            SparkSchema(words(0), words(1))
        })
        // 因为是map，所以返回的一个数组
        sparkSchemas
    }




    def getConnection(host:String,port:String,table:String,tmpDir:String = "/tmp"):Connection = {
        val conf = HBaseConfiguration.create
        conf.set("hbase.zookeeper.quorum", host)
        conf.set("hbase.zookeeper.property.clientPort", port)
        conf.set(TableOutputFormat.OUTPUT_TABLE, table)
        //插入的时临时文件存储位置,大数据量需要
        conf.set("mapreduce.output.fileoutputformat.outputdir",tmpDir)

        val conn = ConnectionFactory.createConnection(conf)
        conn
    }

    def closeConnection(conn:Connection): Unit ={
        if(conn != null){
            conn.close()
        }
    }
}
