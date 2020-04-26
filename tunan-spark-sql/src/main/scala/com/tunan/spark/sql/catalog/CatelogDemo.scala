package com.tunan.spark.sql.catalog

import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.catalog.{Catalog, Database}

object CatelogDemo {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().master("local[2]").appName(this.getClass.getSimpleName).enableHiveSupport().getOrCreate()

        val catalog: Catalog = spark.catalog

        val dbList: Dataset[Database] = catalog.listDatabases()

//        dbList.show(false)

//        println(catalog.currentDatabase)
        import spark.implicits._
//        dbList.map(_.name).show()

        catalog.setCurrentDatabase("offline_dw")
//        val listTable = catalog.listTables()
//        listTable.filter('name === "dws_country_traffic").show(false)

//        println(catalog.isCached("dws_country_traffic"))
//        println(catalog.cacheTable("dws_country_traffic"))
//        println(catalog.isCached("dws_country_traffic"))
//        println(catalog.uncacheTable("dws_country_traffic"))

        spark.udf.register("udf_string_length",(word:String) => {
            word.split(",").length
        })

        catalog.listFunctions().filter('name === "udf_string_length").show(false)
    }
}