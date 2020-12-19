package com.tunan.scala.chapter19

class Person(val name:String){
    override def toString: String = name
}

class Student(override val name: String) extends Person(name){
    override def toString: String = name
}


object UpperBound {

    def printlnArr[T <: Person](arr:Array[T]): Unit = arr.foreach(x => println(x.toString))

    def main(args: Array[String]): Unit = {

        val arr1 = Array(1,2,3,4)
        val arr2 = Array(new Student("zs"),new Student("ls"),new Student("ww"))

//        println(printlnArr(arr1))
        printlnArr(arr2)

    }
}
