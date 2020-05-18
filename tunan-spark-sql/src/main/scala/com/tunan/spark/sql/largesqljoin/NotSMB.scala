package com.tunan.spark.sql.largesqljoin

import org.apache.spark.sql.SparkSession

object NotSMB {

    def main(args: Array[String]): Unit = {


        val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
        .config("spark.sql.join.preferSortMergeJoin", "true")
        .config("spark.sql.autoBroadcastJoinThreshold", "1")
        .config("spark.sql.defaultSizeInBytes", "100000")
          .getOrCreate()
        val sc = spark.sparkContext

        val rdd1 = sc.parallelize(List(("张三", 10), ("李四", 16), ("王五", 20)))
        val rdd2 = sc.parallelize(List(("张三", "老张"), ("李四", "老李"), ("王五", "老王")))


        val Par1 = rdd1.repartition(3).mapPartitions(partition => {
            partition.map(row => {
                Person(Name(row._1,0.1D,b = true), row._2)
            })
        })
        val Par2 = rdd2.repartition(3).mapPartitions(partition => {
            partition.map(row => {
                Alias(Name(row._1,0.1D,b = true), row._2)
            })
        })

        import spark.implicits._
        Par1.toDF().createOrReplaceTempView("person")
        Par2.toDF().createOrReplaceTempView("alias")

        val sql = spark.sql("select * from person p inner join alias a on p.name = a.name")
//        sql.queryExecution.debug.codegen()
        sql.show()

        println(sql.queryExecution.debug.codegen())





        //    }
        //    private def toAssertRowInterval(row: Row): String ={
        //          val orderId = row.getInt(0)
        //          val orderCustomerId = row.getAs[CalendarInterval](1)
        //          val orderAmount = row.getDouble(2)
        //          val customerId = row.getAs[CalendarInterval](3)
        //          val customerLogin = row.getString(4)
        //          s"${orderId}-${orderCustomerId.months}:${orderCustomerId.milliseconds()}-"+
        //          s"${orderAmount}-${customerId.months}:${customerId.milliseconds()}-${customerLogin}"
        //    }
        //     
        //    private def toAssertRow(row: Row): String = {
        //          val orderId = row.getInt(0)
        //          val orderCustomerId = row.getInt(1)
        //          val orderAmount = row.getDouble(2)
        //          val customerId = row.getInt(3)
        //          val customerLogin = row.getString(4)
        //          s"${orderId}-${orderCustomerId}-${orderAmount}-${customerId}-${customerLogin}"
        //    }

    }
    case class Person(name: Name, age: Int)
    case class Alias(name: Name, alia: String)
    case class Name(name: String,a:Double,b:Boolean)
}