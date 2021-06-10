package com.tunan.spark.sql.dim

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.{Calendar, Locale}

object dim_time {

    private val cal: Calendar = Calendar.getInstance(Locale.CHINA)
    cal.set(2021,3,8)

    private val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private val lunarCalendar = new LunarUtil(cal)

    def main(args: Array[String]): Unit = {

        println("年份: "+ getYearDate)
        println("月份: "+ getMonthDate)
        println("日期: "+ getDayDate)
        println("星期: "+ getNameDay)
        println("季度: "+ getQuarterDate)
        println("一年中第几周: "+ getWeekOfYear)
        println("一年中第几天: "+ getDayOfYear)
        println("这月有多少天: "+ getDaysInMonth)
        println("这周第几天: "+ getDayOfWeek)
        println("是否闰年: "+ isLeapYear)
        println("是否月初: "+ isMonthStart)
        println("是否月末: "+ isMonthEnd)
        println("是否今年第一天: "+ isYearStart)
        println("是否今年最后一天: "+ isYearEnd)
        println("是否季度第一天: "+ isQuarterStart)
        println("是否季度最后一天: "+ isQuarterEnd)
        println("是否今年第一天: "+ isYearStart)
        println("农历: "+ LunarDate)
        println("生肖: "+ sxYear)
        println("干支纪年: "+ gzYear)


        println("时间: "+ getCreateTime)


        //        for (i <- 1 to 3){
        //            println(getYearDate)
        //            println(getMonthDate)
        //            println(getDayDate)
        //            println(getNameDay)
        //            cal.add(Calendar.DATE,1)
        //            println(" =========== ")
        //        }


    }


    // 年份
    def getYearDate: Any = {
        cal.get(Calendar.YEAR)
    }

    // 月份
    def getMonthDate: Any = {
        cal.get(Calendar.MONTH) + 1
    }

    // 日期
    def getDayDate: Any = {
        cal.get(Calendar.DATE)
    }

    // 季度
    def getQuarterDate: Any = {
        val month = cal.get(Calendar.MONTH)
        if (month >= 0 && month <= 2) {
            1
        } else if (month >= 3 && month <= 5) {
            2
        } else if (month >= 6 && month <= 8) {
            3
        } else if (month >= 9 && month <= 11) {
            4
        }
    }

    // 星期
    def getNameDay: Any = {
        cal.get(Calendar.DAY_OF_WEEK) - 1
    }

    // 一年中第几周
    def getWeekOfYear: Any ={
        cal.get(Calendar.WEEK_OF_YEAR)
    }

    // 一年中第几天
    def getDayOfYear: Any ={
        cal.get(Calendar.DAY_OF_YEAR)
    }

    // 这月有多少天
    def getDaysInMonth: Any ={
        cal.getActualMaximum(Calendar.DATE)
    }

    // 这周第几天
    def getDayOfWeek: Any ={
        cal.get(Calendar.DAY_OF_WEEK) - 1
    }

    // 是否闰年
    def isLeapYear: Boolean ={
        val tmp_cal: Calendar = cal.clone().asInstanceOf[Calendar]
        tmp_cal.set(tmp_cal.get(Calendar.YEAR), 2, 1)  // 线程不安全
        tmp_cal.add(Calendar.DAY_OF_MONTH, -1)
        tmp_cal.get(Calendar.DAY_OF_MONTH) == 29
    }

    // 是否月初
    def isMonthStart: Any ={
        cal.get(Calendar.DAY_OF_MONTH) == 1
    }

    // 是否月末
    def isMonthEnd: Any ={
        val tmp_cal: Calendar = cal.clone().asInstanceOf[Calendar]
        tmp_cal.add(Calendar.DATE,1)
        tmp_cal.get(Calendar.DATE) == 1
    }

    // 是否季度第一天
    def isQuarterStart: Any ={
        // 月份  、 日期
        val tmp_cal = cal.clone().asInstanceOf[Calendar]
        val month = tmp_cal.get(Calendar.MONTH)
        val date = tmp_cal.get(Calendar.DATE)
        (month == 0 || month == 3 || month == 6 || month == 9) && date == 1
    }

    // 是否季度最后一天
    def isQuarterEnd: Any ={
        // 月份  、 日期
        val tmp_cal = cal.clone().asInstanceOf[Calendar]
        val month = tmp_cal.get(Calendar.MONTH)
        tmp_cal.add(Calendar.DATE,1)
        val date = tmp_cal.get(Calendar.DATE)
        (month == 2 || month == 5 || month == 8 || month == 11) && date == 1
    }

    // 是否今年第一天
    def isYearStart: Any ={
        // 月份 、 日期
        val month = cal.get(Calendar.MONTH)
        val date = cal.get(Calendar.DATE)
        month == 0 && date == 1
    }

    // 是否今年最后一天
    def isYearEnd: Any ={
        // 月份  、 日期
        val tmp_cal = cal.clone().asInstanceOf[Calendar]
        val month = tmp_cal.get(Calendar.MONTH)
        tmp_cal.add(Calendar.DATE,1)
        month == 11 && tmp_cal.get(Calendar.DATE) == 1
    }


    // 农历日期
    def LunarDate: Any ={
        lunarCalendar.toString
    }

    // 生肖年
    def sxYear: Any ={
        lunarCalendar.zodiacYear()
    }

    // 干支纪年
    def gzYear: Any ={
        lunarCalendar.cyclicalYear
    }

    // 干支纪日
    def gzDay: Any ={
        lunarCalendar
    }










    // 获取现在时间
    def getCreateTime: Any = {
        format.format(cal.getTime)
    }
}
