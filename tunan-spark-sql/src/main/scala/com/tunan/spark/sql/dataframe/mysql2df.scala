package com.tunan.spark.sql.dataframe

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{DataFrame, SparkSession}

object mysql2df {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .config("spark.some.config.option", "some-value")
            .getOrCreate()


        val conf = ConfigFactory.load()
        val driver = conf.getString("db.default.driver")
        val url = conf.getString("db.default.url")
        val user = conf.getString("db.default.user")
        val password = conf.getString("db.default.password")
        val source = conf.getString("db.default.source")
        val target = conf.getString("db.default.target")
        val db = conf.getString("db.default.db")

        val df: DataFrame = spark.read
            .format("jdbc")
            .option("url", url)
            .option("dbtable", s"$db.$source")
            .option("user", user)
            .option("password", password)
            .option("driver", driver)
            .load()

        
        //使用DataFrame创建临时表提供spark.sql查询
        df.createOrReplaceTempView("phone_type_dist")

        //spark.sql写SQL返回一个DataFrame
        val sqlDF: DataFrame = spark.sql("select * from phone_type_dist where phoneSystemType = 'IOS'")

        sqlDF.show(false)
        //接着上面返回的sqlDF: DataFrame
/*        sqlDF.write
            .format("jdbc")
            .option("url", url)
            .option("dbtable", s"$db.$target")
            .option("user", user)
            .option("password", password)
            .option("driver",driver)
            .save()*/
    }
}
