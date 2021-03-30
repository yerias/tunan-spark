package com.tunan.spark.listener

import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.scheduler.{SparkListener, SparkListenerJobEnd, SparkListenerTaskEnd}
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

import scala.collection.mutable

class MySparkListener(conf:SparkConf) extends SparkListener with Logging{

    //监听每个Task结束

    override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
        //获取应用程序名称
        val appName = conf.get("spark.app.name")

        //获取作业的taskMetrics
        val metrics = taskEnd.taskMetrics

        //使用对象接收参数
        val metricsObject = Metrics(appName,taskEnd.stageId, taskEnd.taskInfo.taskId, metrics.inputMetrics.bytesRead, metrics.shuffleReadMetrics.totalBytesRead, metrics.outputMetrics.bytesWritten, metrics.shuffleWriteMetrics.bytesWritten)

        //输出字符串类型的metricsObject
        logError(metricsObject.toString)

        //输出Json类型的metricsObject
        logError(Json(DefaultFormats).write(metricsObject))
    }

    //定义case class对象
    case class Metrics(appName:String,stageId:Long,taskId:Long,bytesRead:Long,bytesWritten:Long,shuffleReadMetrics:Long,shuffleWriteMetrics:Long){
        override def toString: String = s"appName:$appName,stageId:$stageId,taskId:$taskId,bytesRead:$bytesRead,bytesWritten:$bytesWritten,shuffleReadMetrics:$shuffleReadMetrics,shuffleWriteMetrics:$shuffleWriteMetrics"
    }
}