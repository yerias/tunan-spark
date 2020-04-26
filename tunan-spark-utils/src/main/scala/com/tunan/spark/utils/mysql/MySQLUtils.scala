package com.tunan.spark.utils.mysql

import org.slf4j.{Logger, LoggerFactory}
import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs

import scala.collection.mutable.ListBuffer

object MySQLUtils{
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)


  def connect(): Unit ={
    DBs.setupAll()	//初始化配置
//    logger.error("=======================连接初始化成功"+DBs.setupAll().toString)
  }


  //插入数据
  def txInsert(sql:String): Unit = {
    //事物插入
    DB.localTx {
      implicit session =>
          SQL(sql).update().apply()
    }
  }


  //插入数据
  def insert(sql:String): Unit = {
    DB.autoCommit {
      implicit session =>
        SQL(sql)
          .update()
          .apply()
    }
  }


  //查询操作
  def select(sql:String): ListBuffer[Any] = {
    val list = new ListBuffer[Any]()
    DB.readOnly {
      implicit session =>
        SQL(sql)
          .map(bean => list.append(bean))
          .update()
          .apply()
    }
    list
  }

  //更新操作
  def update(sql:String) {
    DB.autoCommit {
      implicit session =>
        SQL(sql)
          .update()	//更新操作
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

  def close(): Unit ={
    DBs.closeAll()
//    logger.error("========================连接成功断开"+DBs.closeAll().toString)
  }

  // 测试用
  def main(args: Array[String]): Unit = {

//    insert("insert into dstream_cnt(name,cnt) values(?,?)","aa",10)

  }
}
