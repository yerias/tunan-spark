package com.tunan.spark.sql.extds.hbase

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Mutation, Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableInputFormat
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}

object SparkHBaseSourceApp {

    var CF = "cf"

    // 主类
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .getOrCreate()

        val dept = spark.read.format("com.tunan.spark.sql.extds.hbase")
            .option("spark.hbase.table.name", "dept")
            .option("spark.hbase.table.schema", "(deptno:string,dname:string,loc:string)")
            .option("spark.hbase.zookeeper.host.port", "aliyun:2181")
            .option("spark.hbase.select.cf", "info")
            .load()

        val emp = spark.read.format("com.tunan.spark.sql.extds.hbase")
            .option("spark.hbase.table.name", "emp")
            .option("spark.hbase.table.schema", "(empno:string,ename:string,job:string,mgr:string,hiredate:string,sal:double,comm:double,deptno:string)")
            .option("spark.hbase.zookeeper.host.port", "aliyun:2181")
            .option("spark.hbase.select.cf", "info")
            .load()

        emp.show()

        dept.createOrReplaceTempView("dept")
        emp.createOrReplaceTempView("emp")

        //        emp.show()
        import spark.implicits._

        val conn = HBaseDataSourceUtils.getConnection("aliyun", "2181", "staff")
        val conf = conn.getConfiguration

        val job = Job.getInstance(conf)
        job.setOutputKeyClass(classOf[ImmutableBytesWritable])
        job.setOutputValueClass(classOf[Result])
        job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])


        val sqlRDD = spark.sql(
            """
              |SELECT
              | a.empno,a.ename,a.job,a.mgr,a.hiredate,a.sal,a.comm,b.dname,b.loc
              |FROM
              | dept b
              |JOIN
              | emp a
              |ON a.deptno = b.deptno
              |""".stripMargin).rdd


            saveToHBase(sqlRDD,"empno","ename:string,job:string,mgr:string,hiredate:string,sal:double,comm:double,dname:string,loc:string")
            // 直接Bulk Load数据到Hbase
            .saveAsNewAPIHadoopDataset(job.getConfiguration)

//        val staff = spark.read.format("com.tunan.spark.sql.extds.hbase")
//            .option("spark.hbase.table.name", "staff")
//            .option("spark.hbase.table.schema", "(empno:string,ename:string,job:string,mgr:string,hiredate:string,sal:double,comm:double,dname:string,loc:string)")
//            .option("spark.hbase.zookeeper.host.port", "aliyun:2181")
//            .option("spark.hbase.select.cf", "cf")
//            .option("spark.hbase.row.range", "7844->7903")
//            .load()
//
//        staff.show()


        spark.stop()
    }

    def saveToHBase(rdd:RDD[Row],key:String,value:String): RDD[(ImmutableBytesWritable, Mutation)] ={
        rdd.map(row => {

            val put = new Put(Bytes.toBytes(row.getAs[String](key)))
            val splits = value.split(",")

            for(i <- 0 until splits.length){
                val words = splits(i).split(":")
                words(1).toLowerCase() match {
                    case "string" =>
                        put.addColumn(Bytes.toBytes(CF), Bytes.toBytes(words(0)), Bytes.toBytes(row.getAs[String](words(0))))

                    case "double" =>
                        put.addColumn(Bytes.toBytes(CF), Bytes.toBytes(words(0)), Bytes.toBytes(row.getAs[Double](words(0)).toString))

                    case "int" =>
                        put.addColumn(Bytes.toBytes(CF), Bytes.toBytes(words(0)), Bytes.toBytes(row.getAs[Int](words(0)).toString))
                }
            }
            (new ImmutableBytesWritable,put)
        })
    }
}
