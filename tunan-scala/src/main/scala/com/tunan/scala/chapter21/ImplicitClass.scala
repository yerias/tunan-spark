package com.tunan.scala.chapter21

// 原有封装好的类
class Student(){
    def printName(a:String): Unit ={
        println(a)
    }
}

object ImplicitClass {

    // 需要再原有类上进行增强操作
    implicit class String2Student(val s:Student){
        def increment(): Unit = println("增强的方法")
    }
}


object Main extends App{
    import com.tunan.scala.chapter21.ImplicitClass._
    // 创建一个原来的类，调用增强的方法
    private val student = new Student()
    student.increment()
}
