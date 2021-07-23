package com.tunan.doris.sql

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}

object InsertData {

    val url = s"jdbc:mysql://aliyun:9030/example_db?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false"
    val driver = "com.mysql.jdbc.Driver"

    def main(args: Array[String]): Unit = {

        val schema = StructType(
            StructField("userId", DataTypes.StringType) ::
                StructField("userName", DataTypes.StringType) ::
                StructField("phone", DataTypes.StringType) ::
                StructField("registerTime", DataTypes.StringType) ::
                StructField("location", DataTypes.StringType) ::
                StructField("registerExamName", DataTypes.StringType) ::
                StructField("province", DataTypes.StringType) ::
                StructField("city", DataTypes.StringType) ::
                StructField("originSource", DataTypes.StringType) ::
                StructField("platform", DataTypes.StringType) ::
                StructField("registerChannel", DataTypes.StringType) ::
                StructField("focusExamName", DataTypes.StringType) ::
                StructField("urlType", DataTypes.StringType) ::
                StructField("originUrl", DataTypes.StringType) ::
                StructField("registerType", DataTypes.StringType) ::
                StructField("columnType", DataTypes.StringType) ::
                StructField("lastLoginTime", DataTypes.StringType) ::
                StructField("lastOrderTime", DataTypes.StringType) ::
                StructField("lastPayTime", DataTypes.StringType) ::
                StructField("isPay", DataTypes.StringType) ::
                StructField("payNum", DataTypes.IntegerType) ::
                StructField("realPay", DecimalType.SYSTEM_DEFAULT) ::
                StructField("isCoursePay", DataTypes.StringType) ::
                StructField("coursePayNum", DataTypes.IntegerType) ::
                StructField("courseRealPay", DecimalType.SYSTEM_DEFAULT) :: Nil

        )
        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .getOrCreate()


        val rdd = spark.sparkContext.textFile("tunan-doris/data/student.txt")
        val student = rdd.map(row => {
            val words = row.split(",")
            Row(words(0).toInt, words(1), words(2))
        })

        //        val use: util.List[User] = MakeData.getUser
        //
        //        import collection.JavaConverters._
        //        val user: RDD[_] = spark.sparkContext.parallelize(use.asScala).repartition(5)
        //        val result = spark.createDataFrame(user, classOf[User])

        //        val df: DataFrame = spark.read
        //            .format("jdbc")
        //            .option("url", url)
        //            .option("dbtable", "example_db.student")
        //            .option("user", "doris")
        //            .option("password", "doris")
        //            .option("driver", driver)
        //            .load()
        //        result.write
        //            .format("jdbc")
        //            .mode(SaveMode.Append)
        //            .option("url", url)
        //            .option("dbtable", "example_db.user_profile")
        //            .option("user", "doris")
        //            .option("password", "doris")
        //            .option("driver",driver)
        //            .option("batchsize",2000)
        //            .option("doris.batch.size",2000)

        //            .save()


        spark.close()
    }
}
