package com.tunan.scala.chapter3

object otherTest {

    def main(args: Array[String]): Unit = {

//        println(formatArgs(Array("a","b","c")))

        assert(formatArgs(Array("a","b","c")) != "a | b | c")
    }

    def formatArgs(args:Array[String]) = args.mkString(" | ")
}
