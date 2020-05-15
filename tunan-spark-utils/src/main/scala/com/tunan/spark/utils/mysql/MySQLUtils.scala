package com.tunan.spark.utils.mysql

import org.slf4j.{Logger, LoggerFactory}
import scalikejdbc.config.DBs
import scalikejdbc.{ConnectionPool, DB, SQL}

import scala.collection.mutable.ListBuffer

object MySQLUtils extends Serializable {
    private val logger: Logger = LoggerFactory.getLogger(this.getClass)

    case class SparkSqlConfig(id:Int,query:String,from_db:String,from_table:String,from_partition:String,target_db:String,target_table:String,target_partition:String,executors_num:String,executor_cores:String,executor_memory:String,overwrite:Boolean,yarn:Boolean,master:Boolean)

    def connect(): Unit = {
        DBs.setup() //初始化配置
        logger.error("=======================连接初始化成功" + DBs.hashCode())
    }

    //插入数据
    def txInsert(sql: String): Unit = {
        //事物插入
        DB.localTx {
            implicit session =>
                SQL(sql).update().apply()
        }
    }


    //插入数据
    def insert(sql: String): Unit = {
        DB.autoCommit {
            implicit session =>
                SQL(sql)
                  .update()
                  .apply()
        }
    }

    //插入数据
    def insertByBatch(sql: String, list: ListBuffer[Seq[(Symbol, Any)]]): Unit = {
        DB.localTx {
            implicit session =>
                SQL(sql).batchByName(list: _*).apply()
        }
    }

    //查询操作
    def select(sql: String): List[SparkSqlConfig] = {
        DB.readOnly {
            implicit session =>
                SQL(sql)
                  .map(rs => SparkSqlConfig(rs.int("id"),rs.string("query"),rs.string("from_db"),
                      rs.string("from_table"),rs.string("from_partition"),rs.string("target_db"),rs.string("target_table"),rs.string("target_partition"),
                      rs.string("executors_num"),rs.string("executor_cores"),rs.string("executor_memory"),rs.boolean("overwrite"),rs.boolean("yarn"),rs.boolean("master")))
                  .list()
                  .apply()
        }
    }
    //查询操作
    def select(): List[Employer] = {
        DB.readOnly {
            implicit session =>
                SQL("select * from employer")
                  .map(rs => Employer(rs.string("name"), rs.int("age"), rs.string("salary")))
                  .list()  //结果转换成list
                  .apply()
        }
    }
    case class Employer(name: String, age: Int, salary: String)


    //更新操作
    def update(sql: String) {
        DB.autoCommit {
            implicit session =>
                SQL(sql)
                  .update() //更新操作
                  .apply()
        }
    }

    //根据条件删除
    def deleteBy(sql: String): Unit = {
        DB.autoCommit {
            implicit session =>
                SQL(sql)
                  .update()
                  .apply()
        }
    }

    def close(): Unit = {
        DBs.close()
        logger.error("========================连接成功断开" + DBs.hashCode())
    }

    // 测试用
    def main(args: Array[String]): Unit = {

        connect()
//        val results = select()
//        for (employer <- results) {
//            println(employer.name, employer.age, employer.salary)
//        }

        val buffer = select("select * from tunan.spark_sql_config")

        buffer.foreach(println)

        close()

    }
}
