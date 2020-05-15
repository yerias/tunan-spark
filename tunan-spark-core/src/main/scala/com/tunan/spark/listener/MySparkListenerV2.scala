package com.tunan.spark.listener

import com.tunan.spark.utils.mail.MsgUtils
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.scheduler.{SparkListener, SparkListenerJobStart, SparkListenerTaskEnd}


//TODO 这里可以使用apply优化
class MySparkListenerV2(conf: SparkConf) extends SparkListener with Logging {


    var jobId:Long = _

    override def onJobStart(jobStart: SparkListenerJobStart): Unit = {
        jobId = jobStart.jobId
    }

    println("============准备插入数据============")
    //监听每个Task结束
    override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
        //获取应用程序名称
        val appName = conf.get("spark.app.name")

        //获取作业的taskMetrics
        val metrics = taskEnd.taskMetrics

        //使用对象接收参数
        val listener = Listener(appName, jobId, taskEnd.stageId, taskEnd.taskInfo.taskId, metrics.inputMetrics.bytesRead, metrics.outputMetrics.bytesWritten, metrics.shuffleReadMetrics.totalBytesRead, metrics.shuffleWriteMetrics.bytesWritten)
        ListenerCURD.insert(listener)

        if (!"SUCCESS".equals(taskEnd.taskInfo.status)){
            MsgUtils.send("971118017@qq.com", "ERROR：数据异常", s"jobID: $jobId 数据异常，请马上检查: ${listener.toString}")
        }
    }
    println("============成功插入数据============")
}
