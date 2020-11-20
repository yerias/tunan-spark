package com.tunan.scala.chapter3

import scala.collection.mutable

object MapTest {

    def main(args: Array[String]): Unit = {

//        immutableMap
        mutableHashMap

    }

    def mutableHashMap(): Unit ={
        val map = mutable.HashMap(
            "1" -> "a",
            "2" -> "b",
            "3" -> "c"
        )

        // 可变对象还是在原来的引用上
        map += ("4" -> "d")

        println(map.mkString(" | "))
        println(map("2"))
    }

    def immutableMap(): Unit ={
        val map = Map(
            "1" -> "a",
            "2" -> "b",
            "3" -> "c"
        )

        // 不可变必须赋值给新对象
        val newMap = map + ("4" -> "d")

        println(newMap.mkString(" | "))
    }
}
