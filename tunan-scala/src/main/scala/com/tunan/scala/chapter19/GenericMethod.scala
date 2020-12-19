package com.tunan.scala.chapter19


/*
    泛型方法
    泛型方法在调用方法的时候 明确具体的数据类型.
 */
object GenericMethod {
    def main(args: Array[String]): Unit = {
        val arr1 = Array(1, 2, 3, 4, 5, 6)
        val arr2 = Array("a", "b", "c", "d", "e", "f")

        println(getMiddleElement(arr1))
        println(getMiddleElement(arr2))
    }


    // 泛型解决的方法通用性的问题
    def getMiddleElement[T](arr: Array[T]): T = {
        arr(arr.length / 2)
    }
}
