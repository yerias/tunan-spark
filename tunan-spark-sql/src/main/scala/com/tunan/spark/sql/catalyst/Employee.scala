package com.tunan.spark.sql.catalyst

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object Employee {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getSimpleName)
        val sc = new SparkContext(conf)

        val spark = SparkSession
          .builder()
          .config(conf)
          .getOrCreate()


        import spark.implicits._

        val df = sc
          .parallelize(Employee("张三", 18, 10001) :: Employee("李四", 19, 10002) :: Employee("王五", 20, 10003) :: Nil).toDF()

        df.createOrReplaceTempView("employee")


    }
    case class Employee(name:String,age:Int,provinceId:Int)
}
