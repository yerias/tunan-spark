// Copyright (c) 2021 Beijing Dingshi Zongheng Technology Co., Ltd. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// See the License for the specific language governing permissions and
// limitations under the License.

package com.tunan.doris.spark

import com.tunan.doris.utils.{Consts, LoggerUtil, MySrSink}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
* @Description spark按分区数批量提交到数据库
* @Date 8:32 2021/12/31
* @Param
* @return
**/
object SparkConnector2StarRocks {
    // parameters
    val starRocksName = "test"
    val tblNameSrc = "student_stream_load"
    val tblNameDst = "student_stream_load2"
    val userName = "root"
    val password = "root"
    val srFe = "nn" // fe hostname
    val port = 18030 // fe http port
    val filterRatio = 0.2
    val columns = "id,name,year,month,day"
    val master = "local[2]"
    val appName = "SparkConnector2StarRocks"
    val partitions = 2 // computing parallelism
    val buckets = 1 // sink parallelism
    val debug = false

    LoggerUtil.setSparkLogLevels()

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
            .setAppName(appName)
            .setMaster(master)
            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        val spark = SparkSession.builder().config(conf).master(master).enableHiveSupport().getOrCreate()
        val sc = spark.sparkContext
        sc.setLogLevel("WARN")
        import spark.implicits._

        val starrocksSparkDF = spark.read.format("starrocks")
            .option("starrocks.table.identifier", s"${starRocksName}.${tblNameSrc}")
            .option("starrocks.fenodes", s"${srFe}:${port}")
            .option("user", s"${userName}")
            .option("password", s"${password}")
            .load().repartition(partitions)

        starrocksSparkDF.map(x => x.toString().replaceAll("\\[|\\]", "").replace(",", Consts.starrocksSep))
            .repartition(buckets).foreachPartition(
            itr => {
                val sink = new MySrSink(Map(
                    "max_filter_ratio" -> s"${filterRatio}",
                    "columns" -> columns,
                    "column_separator" -> Consts.starrocksSep),
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
            }
        )

        spark.stop()
    }
}