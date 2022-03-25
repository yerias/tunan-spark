package com.tunan.graphx

import org.apache.spark.sql.SparkSession

/**
 * @Auther: 李沅芮
 * @Date: 2022/3/25 15:19
 * @Description:
 */
object idMappingDoris {

    val starRocksName = "sys"
//    val tblNameSrc = "student_stream_load"
    val tblNameDst = "id_mapping"
    val userName = "root"
    val password = "123456"
    val srFe = "aliyun" // fe hostname
    val port = 8030 // fe http port
    val filterRatio = 0.2
    val columns = "user_id,device,cnt"
    val master = "local[2]"
    val appName = "SparkConnector2StarRocks"
    val partitions = 4 // computing parallelism
    val buckets = 4 // sink parallelism
    val debug = true


    def main(args: Array[String]): Unit = {

        val spark = SparkSession.builder()
            .master("local[4]")
            .appName(this.getClass.getSimpleName)
            .getOrCreate()

        import spark.implicits._

        val file = spark.sparkContext.textFile("tunan-spark-graphx/data/mapping.txt")
        val df = file.map(row => {
            val lines = row.split(",")
            idMapping(lines(0), lines(1))
        }).toDF()

        df.map(x => x.toString().replaceAll("\\[|\\]", "").replace(",", Consts.starrocksSep))
            .repartition(buckets).foreachPartition(
            itr => {
                val sink = new MySrSink(Map(
                    "max_filter_ratio" -> s"${filterRatio}",
                    "columns" -> columns,
                    "column_separator" -> Consts.starrocksSep,
                    "strict_mode" -> "false"),
                    starRocksName,
                    userName,
                    password,
                    tblNameDst,
                    srFe,
                    port,
                    debug,
                    debug)
                if (itr.hasNext) sink.invoke(
                    itr.mkString("\n")
                )
                println("结束！")
            })

    }
    case class idMapping(user_id: String, device_id: String, cnt: Int = 1)
}
