package com.tunan.scala.option

import scala.collection.mutable

object OptionMapTest {

    def main(args: Array[String]): Unit = {

        val map = mutable.Map("name" -> "法外狂徒-张三","age" -> "18")

        val name: Option[String] = map.get("name")
        val school: Option[String] = map.get("school")

        println(isNotNone(school))

        println(school.getOrElse("一中"))

        println(school.isEmpty)



    }

    def isNotNone(option: Option[String]): String ={
        option match {
            case Some(s) => s
            case None => "..."
        }
    }

}
