package com.tunan.scala.chapter21

class Student2 {
    def toTeacher: Teacher = {
        new Teacher
    }
}

class Teacher {
    override def toString: String = "我是一个Teacher"

    def printString(): Unit = {
        println("hello world")
    }
}


object ImplicitMethod {

    def main(args: Array[String]): Unit = {
        // 隐式方法，只与方法参数和返回值类型有关
        implicit def int2Double(a: Int): Double = a.toDouble

        implicit def Int2Double(a: Student2): Teacher = {
            a.toTeacher
        }

        val a: Student2 = new Student2
        a.printString() // 4.0

        val b: Double = 4
        println(b)
    }
}
