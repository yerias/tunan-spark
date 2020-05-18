package com.tunan.spark.listener

import scalikejdbc.config.DBs
import scalikejdbc.{DB, SQL}

case class Listener(app_name: String, job_id: Long, stage_id: Long, task_id: Long, file_read_byte: Long, file_write_byte: Long, shuffle_read_byte: Long, shuffle_write_byte: Long){
    override def toString: String = s"app_name:$app_name,job_id:$job_id,stage_id:$stage_id,task_id:$task_id,file_read_byte:$file_read_byte,file_write_byte:$file_write_byte,shuffle_read_byte:$shuffle_read_byte,shuffle_write_byte:$shuffle_write_byte"
}

object ListenerCURD {

    def Before(): Unit = {
        //初始化配置
        DBs.setupAll()
    }

    def insert(listener: Listener): Unit = {
        Before()
        //事物插入
        DB.localTx {
            implicit session => {
                SQL("insert into wc2mysql(app_name,job_id,stage_id,task_id,file_read_byte,file_write_byte,shuffle_read_byte,shuffle_write_byte) values(?,?,?,?,?,?,?,?)")
                    .bind(listener.app_name,listener.job_id, listener.stage_id, listener.task_id, listener.file_read_byte, listener.file_write_byte, listener.shuffle_read_byte, listener.shuffle_write_byte)
                    .update() //更新操作
                    .apply()
            }
        }
        After()
    }

    def After(): Unit = {
        //关闭资源
        DBs.closeAll()
    }
}
