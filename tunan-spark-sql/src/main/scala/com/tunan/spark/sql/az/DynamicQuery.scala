package com.tunan.spark.sql.az

import com.tunan.spark.utils.mysql.MySQLUtils
import org.apache.commons.lang3.StringUtils
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object DynamicQuery extends Logging{


    var id:Int = _
    def main(args: Array[String]): Unit = {
        System.setProperty("HADOOP_USER_NAME","hadoop")

        if (args.length ==0){
            throw new IllegalArgumentException("必须输入一个主键")
        }

        id = args(0).toInt

        // 必选
        var select:String = ""

        var fromDB:String = ""
        var fromTable:String = ""

        var targetDB:String = ""
        var targetTable:String = ""

        // 可选
        var fromPartition:String = ""
        var targetPartition:String = ""
        var executorNums:String = ""
        var executorMemory:String = ""
        var executorCore:String = ""
        var overwrite:String = ""
        var yarnModel:String = ""
        var masterModel:String = ""

        try {
            MySQLUtils.connect()
            val bean= MySQLUtils.select(s"select * from tunan.spark_sql_config where id = ${id}").head

            // 预留字段/待处理
            select =  bean.query
            fromDB = bean.from_db
            fromTable = bean.from_table
            fromPartition = bean.from_partition

            // 必选
            targetDB = bean.target_db
            targetTable = bean.target_table
            if (StringUtils.isEmpty(targetDB)||StringUtils.isEmpty(targetTable)){
                logError("没有指定存储的数据库或者表")
            }

            targetPartition = bean.target_partition

            if(StringUtils.isEmpty(bean.executors_num)){
                executorNums = "1"
            }else{
                executorNums = bean.executors_num
            }


            if(StringUtils.isEmpty(bean.executor_cores)){
                executorCore="2"
            }else{
                executorCore = bean.executor_cores
            }

            if(StringUtils.isEmpty(bean.executor_memory)){
                executorMemory = "1GB"
            }else{
                executorMemory = bean.executor_memory
            }


            if(!bean.overwrite){
                overwrite = "Append"
            }else{
                overwrite = "Overwrite"
            }

            if(!bean.yarn){
                yarnModel = "client"
            }else{
                yarnModel = "Cluster"
            }

            if(!bean.master){
                masterModel = "local[2]"
            }else{
                masterModel = "yarn"
            }

        } catch {
            case e:Exception => e.printStackTrace()
        } finally {
            MySQLUtils.close()
        }

        val spark = SparkSession
          .builder()
          .config("spark.master",masterModel)
          .config("deploy-mode",yarnModel)
          .config("executor-memory",executorMemory)
          .config("executor-cores",executorCore)
          .config("num-executors",executorNums)
          .appName(this.getClass.getSimpleName)
          .enableHiveSupport()
          .getOrCreate()


        println("executor-memory: "+spark.conf.get("executor-memory"))
        println("executor-cores: "+spark.conf.get("executor-cores"))
        println("num-executors: "+spark.conf.get("num-executors"))
        println("spark.master: "+spark.conf.get("spark.master"))
        println("deploy-mode: "+spark.conf.get("deploy-mode"))


        var sql: DataFrame = null
        sql = spark.sql(s"select ${select} from ${fromDB}.${fromTable}")

        sql.show()

        sql.write.mode(overwrite).saveAsTable(targetDB+"."+targetTable)

    }
}
