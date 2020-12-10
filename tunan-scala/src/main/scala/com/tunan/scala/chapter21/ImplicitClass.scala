package com.tunan.scala.chapter21

import java.io.File

import scala.io.Source


// 原有封装好的类
class Student(){
    def printName(a:String): Unit ={
        println(a)
    }
}

class Person(){

}

class Test{
    def Person2Student(p1:Person)(implicit a:Person => Student): Unit ={
        p1.printName("a")
    }
}

object ImplicitClass {

    // 需要再原有类上进行增强操作
    implicit class String2Student(val s:Student){
        def increment(): Unit = println("增强的方法")
    }

//    implicit class RichFile(file:File){
//
//        def read(path:String): Unit ={
//            println(Source.fromFile(path))
//        }
//    }
}


object Main extends App{
    import com.tunan.scala.chapter21.ImplicitClass._
    // 创建一个原来的类，调用增强的方法
//    private val student = new Student()
//    student.increment()

    // 对原有的类进行增强
    implicit class RichFile(file:File){

        def read: Unit ={
            println(Source.fromFile(file).mkString)
        }
    }


    implicit def a(a:Person) = new Student
    private val test1 = new Test()
    test1.Person2Student(new Person())



    val path = "tunan-scala/data/hello.txt"
    val file = new File(path)
    file.read

}
