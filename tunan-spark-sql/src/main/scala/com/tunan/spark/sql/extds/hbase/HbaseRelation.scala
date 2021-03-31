package com.tunan.spark.sql.extds.hbase

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}

import scala.collection.mutable.ListBuffer

case class HbaseRelation(@transient sqlContext: SQLContext, @transient parameters: Map[String, String]) extends BaseRelation with TableScan with Serializable with Logging{
    // 拿到外部传入的hbase表名
    private val hbaseTable: String = parameters.getOrElse("spark.hbase.table.name", sys.error("hbase.table.name is required..."))
    // 拿到外部传入的Schema
    private val sparkTableSchema: String = parameters.getOrElse("spark.hbase.table.schema", sys.error("spark.table.schema is required..."))
    // 拿到外部传入的zookeeper地址
    private val zookeeperHostAndPort: String = parameters.getOrElse("spark.hbase.zookeeper.host.port", sys.error("spark.zookeeper.host.port is required..."))
    // 注意 这里可能还需要拿到传入的列族，以实现获取不同列族的数据
    private val cf:String = parameters.getOrElse("spark.hbase.select.cf",sys.error("spark.hbase.select.cf is required..."))

    private val ranges:String = parameters.getOrElse("spark.hbase.row.range","->")

    // TODO 当字符串最后一位或者N位是分隔符时,继续切分,保留空值。
    private val range = ranges.split("->",-1)
    private val startRowKey = range(0).trim
    private val endRowKey = range(1).trim

    // 将传入的Schema信息传入extractSparkFields进行解析，返回一个SparkSchema数组
    private val sparkFields: Array[SparkSchema] = HBaseDataSourceUtils.extractSparkFields(sparkTableSchema)

    override def schema: StructType = {
        // 拿到每一个SparkSchema
        val rows: Array[StructField] = sparkFields.map(field => {
            // 拿到SparkSchema中的fieldType做模式匹配，封装成我们需要的StructField
            val structField = field.fieldType.toLowerCase match {
                case "int" => StructField(field.fieldName, IntegerType)
                case "string" => StructField(field.fieldName, StringType)
                case "long" => StructField(field.fieldName, LongType)
                case "float" => StructField(field.fieldName, FloatType)
                case "double" => StructField(field.fieldName, DoubleType)
            }
            // 返回StructField
            structField
        })
        // 使用的map，返回的一个structField数组，直接放入StructType对象中，即拿到最终的StructType
        new StructType(rows)
    }

    /**
     * 把HBase中的数据转成RDD[Row]
     * 1）怎么样去读取HBase的数据
     * 2）怎么样把HBase的转成RDD[Row]
     */
    override def buildScan(): RDD[Row] = {

        // 创建hbase配置文件
        val hbaseConf = HBaseConfiguration.create()
        // 设置zookeeper的地址
        hbaseConf.set("hbase.zookeeper.quorum", zookeeperHostAndPort)
        // 设置hbase的表名
        hbaseConf.set(TableInputFormat.INPUT_TABLE, hbaseTable)
        hbaseConf.set(TableInputFormat.SCAN_ROW_START, startRowKey)
        hbaseConf.set(TableInputFormat.SCAN_ROW_STOP, endRowKey)

        // TODO TableInputFormat 专门处理基于HBase的MapReduce的输入数据的格式类
        // TODO ImmutableBytesWritable 是一种数据类型，一般作为RowKey使用，也可以读取成字符串
        // TODO Result HBase客户端读取的结果集
        // TODO SparkContext.newAPIHadoopRDD ==> RDD
        // TODO RDD ==> saveAsNewAPIHadoopDataset

        // 通过newAPIHadoopRDD拿到对应hbase表中的所有数据
        val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sqlContext.sparkContext.newAPIHadoopRDD(
            hbaseConf,
            classOf[TableInputFormat],
            classOf[ImmutableBytesWritable],
            classOf[Result])

        // 拿到我们需要的Result
        hbaseRDD.map(_._2).map(result => {
            // 创建一个列表，类型且是Any的，方便后面转换成RDD[Row]
            val buffer = ListBuffer[Any]()

            var flag_key = true
            // 变量每一行数据

            for(i <- sparkFields.indices){
                if(i == 0){
                    buffer += resolveKey(result)
                }else{
                    buffer += resolveColumn(result, sparkFields(i))
                }
            }

            // 得到最终的RDD[Row]
            Row.fromSeq(buffer)
        })
    }
    private def resolveKey(result: Result): Any = {
        Bytes.toString(result.getRow)
    }

    private def resolveColumn(result: Result, field: SparkSchema):Any = {
        // 判断对应列族下的列有没有数据
        val column = if (result.containsColumn(cf.getBytes(), Bytes.toBytes(field.fieldName))) {
                // 如果有，则拿到对应的数据，并且通过模式匹配，转换成对应的类型
                field.fieldType match {
                    case "string" =>
                        Bytes.toString(result.getValue(Bytes.toBytes(cf), Bytes.toBytes(field.fieldName)))
                    case "int" =>
                        Bytes.toString(result.getValue(Bytes.toBytes(cf), Bytes.toBytes(field.fieldName))).toInt
                    case "long" =>
                        Bytes.toString(result.getValue(Bytes.toBytes(cf), Bytes.toBytes(field.fieldName))).toLong
                    case "float" =>
                        Bytes.toString(result.getValue(Bytes.toBytes(cf), Bytes.toBytes(field.fieldName))).toFloat
                    case "double" =>
                        Bytes.toString(result.getValue(cf.getBytes,field.fieldName.getBytes)).toDouble
                }
            } else {
                // 如果没有数据，则对应字段直接存一个空
                field.fieldType match {
                    case "string" => "-"
                    case "int" => 0
                    case "long" => 0L
                    case "float" => 0.0F
                    case "double" => 0.00D
                }
            }
        column
    }
}