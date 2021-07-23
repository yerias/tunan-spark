package com.tunan.doris.utils

import org.apache.log4j.{Level, Logger}
import org.apache.spark.internal.Logging

object LoggerUtil extends Logging {
  def setSparkLogLevels(level:Level = Level.WARN) {
    val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
    if (!log4jInitialized) {
      logInfo(s"Setting log level to [${level.toString}] for Spark")
      Logger.getRootLogger.setLevel(level)
    }
  }

  def warn(str:String): Unit ={
    logWarning(str)
  }

  def error(str:String): Unit ={
    logError(str)
  }
}