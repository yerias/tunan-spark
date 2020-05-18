package com.tunan.spark.sql.largesqljoin

import org.apache.spark.sql.SparkSession

object NotSMBV3 {

    def main(args: Array[String]): Unit = {


/*        import spark.implicits._
        val tokens = Seq(
            (0, "playing"),
            (1, "with"),
            (2, "SortMergeJoinExec")
        ).toDF("id","token")

        tokens.printSchema()

        val q = tokens.join(tokens, Seq("id"), "inner")
        q.queryExecution*/

        val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()
        val sc = spark.sparkContext

        val rdd1 = sc.parallelize(List((1, 10), (2, 16), (3, 20)))
        val rdd2 = sc.parallelize(List((1, "老张"), (2, "老李"), (3, "老王")))

        val Par1 = rdd1.repartition(1).mapPartitions(partition => {
            partition.map(row => {
                Person(row._1, row._2)
            })
        })
        val Par2 = rdd2.repartition(1).mapPartitions(partition => {
            partition.map(row => {
                Alias(row._1, row._2)
            })
        })

        import spark.implicits._
        Par1.toDF().createOrReplaceTempView("person")
        Par2.toDF().createOrReplaceTempView("alias")

        val sql = spark.sql("select * from person p inner join alias a on p.id = a.id")
        sql.show(true)
        sql.queryExecution.debug.codegen()
        sql.explain()

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
    case class Person(id: Int, age: Int)
    case class Alias(id: Int, alia: String)
    case class Name(name: String)
}