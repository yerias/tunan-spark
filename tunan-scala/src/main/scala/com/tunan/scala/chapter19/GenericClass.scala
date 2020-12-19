package com.tunan.scala.chapter19

// 泛型类，在创建对象的时候, 明确具体的数据类型.
class GenericClass[T] (val a:T, val b:T){

    def printElement(ele:T): Unit ={
        println(ele)
    }
}

object GenericClass{
    def main(args: Array[String]): Unit = {

        val generic1 = new GenericClass[String]("zhangsan", "喜欢女孩")
        val generic2= new GenericClass[Int](18, 10)

        println(generic1.a)
        generic1.printElement("hehe")
        println(generic2.a)
        generic2.printElement(15)
    }
}
