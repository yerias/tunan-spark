package com.tunan.spark.sql.largesqljoin

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.collection.mutable

object SmallJoinLargeV2 {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
          .builder()
          .master("local[*]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()

        import spark.implicits._
        val userTable = "tunan-spark-sql/moke1/user_click.txt"
        val productTable = "tunan-spark-sql/moke1/product_category.txt"
        val out = "tunan-spark-sql/out"

        val userDF = spark.read.format("text").load(userTable)
        val productDF = spark.read.format("text").load(productTable)
        val newUserDF = userDF.toDF("user_id", "product_id")
        val newProductDF = productDF.toDF("product_id", "category_id")





        // 分区 分区内hash join
        newUserDF.createOrReplaceTempView("user")
        newProductDF.createOrReplaceTempView("product")

//        newUserDF.printSchema()


    }
    case class UserProduct(userId:String,productId:String)
}
