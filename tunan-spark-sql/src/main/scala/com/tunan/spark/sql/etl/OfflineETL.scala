package com.tunan.spark.sql.etl

import com.tunan.spark.sql.utils.IpParseUtil
import org.apache.spark.sql.{DataFrame, SparkSession}

object OfflineETL {

  var in = "tunan-spark-sql/data/access.json"
  var out = "tunan-spark-sql/out"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .enableHiveSupport()
      .getOrCreate()

    if (args.length>0){
      in = args(0)
      out = args(1)
    }


    etl(spark)

    spark.stop()
  }
  def etl(spark:SparkSession): Unit = {

    val jsonDF: DataFrame = spark.read.option("mode", "DROPMALFORMED").json(in)

    jsonDF.createOrReplaceTempView("json")


    spark.udf.register("get_country", (input: String) => {
      val ip = IpParseUtil.IpParse(input)
      ip.getCountry
    })

    spark.udf.register("get_province", (input: String) => {
      val ip = IpParseUtil.IpParse(input)
      ip.getProvince
    })

    spark.udf.register("get_city", (input: String) => {
      val ip = IpParseUtil.IpParse(input)
      ip.getCity
    })

    spark.udf.register("get_area", (input: String) => {
      val ip = IpParseUtil.IpParse(input)
      ip.getArea
    })

    spark.udf.register("get_brand_category", (input: String) => {
      input.split(" ")(0)
    })

    val accessDF: DataFrame = spark.sql(
      """select
        |	*,
        |	get_country(ip) as country,
        |	get_province(ip) as province,
        |	get_city(ip) as city,
        |	get_area(ip) area,
        |	current_date as lastdate,
        |	get_brand_category(sdkversion) as brand_category
        | from
        |	json""".stripMargin)

//        spark.sql("show databases").show()

/*    accessDF.write.mode("overwrite")
      .format("parquet")
      .saveAsTable("access_dw.ods_access_parquet")*/

    accessDF.write.mode("overwrite")
      .format("parquet").option("compress","LZ4")
  }
}
