package com.tunan.spark.sql.mongo

import java.text.{DateFormat, SimpleDateFormat}
import java.util
import java.util.{Calendar, Date}

import org.apache.spark.sql.types._

object ToolUtils {

    def caseTo(value: String, dataType: DataType): Any = {
        dataType match {
            case _: DoubleType => value.toDouble
            case _: LongType => value.toLong
            case x: DateType => new Date(value)
            case _: IntegerType => value.toInt
            case _: StringType => value
            case _: ArrayType => value.toArray
        }
    }

    def getYesterdayDate: String = {

        val dateFormat: DateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val calendar: Calendar = Calendar.getInstance
        calendar.set(Calendar.HOUR_OF_DAY, -24)
        val yesterdayDate = dateFormat.format(calendar.getTime)
        yesterdayDate
    }

    def getLastMonth: String = {
        val cal: Calendar = Calendar.getInstance
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1)
        val date: Date = cal.getTime
        val format3: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val dateStringYYYYMMDD3: String = format3.format(date)
        dateStringYYYYMMDD3
    }

    def getLastThreeMonth: String = {
        val cal: Calendar = Calendar.getInstance
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -3)
        val date: Date = cal.getTime
        val format3: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val dateStringYYYYMMDD3: String = format3.format(date)
        dateStringYYYYMMDD3
    }

    def getTimeInMillis: Long = {
        val calendar: Calendar = Calendar.getInstance
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        //当天0点
        val zero: Long = calendar.getTimeInMillis
        zero
    }

    def getTimeByYesterdayInMillis: Long = {
        val calendar: Calendar = Calendar.getInstance
        calendar.set(Calendar.HOUR_OF_DAY, -24)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        //当天0点
        val zero: Long = calendar.getTimeInMillis
        zero
    }

    def getToDayByZero: String = {
        val df = new SimpleDateFormat("yyyy-MM-dd 00:00:00")
        val toDay = df.format(new Date())
        toDay
    }

    def getYesterdayByZero: String = {
        val df = new SimpleDateFormat("yyyy-MM-dd 00:00:00")
        val yesterdayDate = df.format(new Date(System.currentTimeMillis - 1000 * 60 * 60 * 24))
        yesterdayDate
    }




    def getMillisFromDay(day: String): Long = {
        val df: DateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val date: Date = df.parse(day) //yyyy-MM-dd
        val cal: Calendar = Calendar.getInstance
        cal.setTime(date)
        val timestamp: Long = cal.getTimeInMillis //单位为毫秒
        timestamp
    }

    def clearDocument(document: String): Unit = {
        MongoConnect.getInstance
        val client = new MongoConnect()
        client.deleteAll("cms", document)
        MongoConnect.close()
    }

    def createIndexFromMap(ducument: String, map: util.Map[String, Object]): Unit = {
        MongoConnect.getInstance
        val client = new MongoConnect()
        client.createIndex(ducument, map)
        MongoConnect.close()
    }

    def getThirtyDayByCurrentTime: String = {
        val now = Calendar.getInstance
        now.add(Calendar.DAY_OF_MONTH, -30)
        val time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime)
        time
    }

    def getMinuteAgoTime : String = {
        var minute = ""
        val date = new Date() //获取当前时间
        val calendar = Calendar.getInstance
        calendar.setTime(date)
        calendar.add(Calendar.MINUTE, -30) // 30分钟前

        //获取到完整的时间
        minute = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime)
        minute
    }

    // 毫秒转日期
    def milliSecondToDay(ms:Long): String ={
        val date = new Date()
        date.setTime(ms)
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
    }

    // 转换特殊时间
    def ParseTime(time:String): String ={
        val dateTime = time.replace("Z", "")
        val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val newTime = format.parse(dateTime)
        val result = defaultFormat.format(newTime)
        result
    }

    def main(args: Array[String]): Unit = {

//        val str:String = "Sat Dec 05 01:38:09 CST 2020"
//        val sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK)
//        val date = sdf1.parse(str)
//        val cal: Calendar = Calendar.getInstance
//        cal.setTime(date)
//        val ca:GregorianCalendar  = new GregorianCalendar(TimeZone.getTimeZone("GMT 00:00"))
//        ca.set(cal.get(Calendar.YEAR),
//            cal.get(Calendar.MONTH),
//            cal.get(Calendar.DAY_OF_MONTH),
//            cal.get(Calendar.HOUR),
//            cal.get(Calendar.MINUTE),
//            cal.get(Calendar.SECOND))
//
//        val format:SimpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        format.setTimeZone(TimeZone.getDefault())
//        System.out.println(format.format(ca.getTime()))

        println(milliSecondToDay(1611820771339L))

//        println(getTimeInMillis)
    }
}
