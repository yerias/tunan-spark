package com.tunan.scala.chapter3

object ArrayTest {

    def main(args: Array[String]): Unit = {

        //        test1
        //        test2
        test3

    }


    def test1: Unit = {
        val array = new Array[String](3)
        array(0) = "hello"
        array(1) = ","
        array(2) = "world"

        for (i <- 0 to 2) {
            print(array(i))
        }
    }

    def test2: Unit = {
        val array = new Array[String](3)
        array.update(0, "hello")
        array.update(1, ",")
        array.update(2, "world")

        for (i <- 0 to 2) {
            print(array.apply(i))
        }
    }
    def test3: Unit = {
        // apply是一个工厂方法
        val array: Array[String] = Array.apply("hello", ",", "world")
        array.foreach(print)
    }
}
