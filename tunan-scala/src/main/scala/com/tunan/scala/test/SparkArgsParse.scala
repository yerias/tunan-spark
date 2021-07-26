package com.tunan.scala.test

import java.util.regex.Pattern

object SparkArgsParse {

    def main(args: Array[String]): Unit = {

        val mainClass = "--conf x=com.wx.history.data.sync.HistoryDataSync --class aaaaa"

        val pattern = Pattern.compile("(--[^=]+)=(.+)")

        val matcher = pattern.matcher(mainClass)

        println(matcher.group(1))
        println(matcher.group(2))


    }
}
