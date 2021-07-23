package com.tunan.doris.spark

import com.tunan.doris.utils.{Consts, MyDorisSink}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


/**
 * doris
 * 2021.07.21
 * 垃圾功能，强制Spark版本，类冲突，请求FE返回BE的内网地址，修改不了ip连接不通
 */

object SparkConnector2DorisDB {
    // parameters
    val dorisDbName = "example_db"
    val tblNameSrc = "student"
    val tblNameDst = "student"
    val userName = "doris"
    val password = "doris"
    val dorisFe = "aliyun" // fe hostname
    val port = 8095 // fe http port
    val filterRatio = 0.1
    val columns = "id,name,sex"
    val master = "local"
    val appName = "tunan2doris"
    val partitions = 2 // computing parallelism
    val buckets = 1 // sink parallelism
    val debug = false
    val url = s"jdbc:mysql://aliyun:9030/example_db?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false"
    val driver = "com.mysql.jdbc.Driver"

    //    LoggerUtil.setSparkLogLevels()

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
            .setAppName(appName)
            //            .setMaster(master)
            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        val spark = SparkSession
            .builder()
            .config(conf)
            //            .master(master)
            .getOrCreate()
        val sc = spark.sparkContext
        sc.setLogLevel("WARN")

        val dorisSparkDF = spark.read.format("doris")
            .option("doris.table.identifier", s"${dorisDbName}.${tblNameSrc}")
            .option("doris.fenodes", s"${dorisFe}:${port}")
            .option("user", s"${userName}")
            .option("password", s"${password}")
            .load().repartition(partitions)
        //        val dorisSparkDF: DataFrame = spark.read
        //            .format("jdbc")
        //            .option("url", url)
        //            .option("dbtable", "example_db.student")
        //            .option("user", "doris")
        //            .option("password", "doris")
        //            .option("driver", driver)
        //            .load()


        dorisSparkDF.createOrReplaceTempView("view_tb1")
        val resDf = spark.sql(
            """
              |select id, n as name, sex
              |from view_tb1
              |lateral view explode(split(name,',')) temp_tbl as n
              |""".stripMargin)

        resDf.show(5, false) // IDEA/REPL local outputs


        resDf.rdd.map(x => x.toString().replaceAll("\\[|\\]", "").replace(",", Consts.dorisdbSep))
            .repartition(buckets)
            .foreachPartition(
                partitions => {
                    val sink = new MyDorisSink(Map(
                        // "label"->"label123" ：
                        //     1. If not customized, DorisDB randomly generates a code as the label;
                        //     2. Stream-load label is 'Unique', the Stream-load with same label can be loaded only once.
                        //        [Good choice]: the label can be combined with info like batch-time and TaskContext.get.partitionId().
                        "max_filter_ratio" -> s"${filterRatio}",
                        "columns" -> columns,
                        "column_separator" -> Consts.dorisdbSep),
                        dorisDbName,
                        userName,
                        password,
                        tblNameDst,
                        dorisFe,
                        port,
                        debug,
                        debug)

                    partitions.foreach(row => {
                        sink.invoke(row.mkString("\n"))
                    })
                }
            )

        spark.close()
    }
}
