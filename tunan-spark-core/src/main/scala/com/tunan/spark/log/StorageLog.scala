package com.tunan.spark.log

import com.tunan.spark.utils.mysql.MySQLDruidUtils
import org.apache.spark.{SparkConf, SparkContext}

object StorageLog {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName).setMaster("local[2]")
        val sc = new SparkContext(conf)

        val file = sc.textFile("tunan-spark-core/data/url.txt")

        file.foreachPartition(partition => {

            val conn = MySQLDruidUtils.getConnection
            conn.setAutoCommit(false)
            val stat = conn.createStatement()

            partition.foreach(row => {
                var words: Array[String] = null
                try{
                    words  = row.split("\t")
                    println(1/0)

                }catch {
                    case ex:Exception => {
                        val log = ex.toString.split(":")(1).trim
                        val ERROR_SQL = s"insert into error_log values (null,'zs','${row}','${log}',0,'2020-12-07')"
                        stat.addBatch(ERROR_SQL)
                        ex.printStackTrace()
                        println(ex)
                    }
                }
            })
            stat.executeBatch()
            conn.commit()

            MySQLDruidUtils.close(null,stat,conn)
        })
        sc.stop()
    }
}
