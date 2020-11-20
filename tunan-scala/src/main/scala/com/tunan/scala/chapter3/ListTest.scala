package com.tunan.scala.chapter3

object ListTest {

    def main(args: Array[String]): Unit = {

        val list1 = List(1, 2, 3, 4)
        val list2 = 11 :: 12 :: 13 :: 14 :: Nil

        val list3 = list1 ::: list2

        println(list3(1))
        println(list3.reverse)
        if(list3.nonEmpty) list3.foreach(print)
        println( )
        println(list3.mkString(" - "))



    }
}
