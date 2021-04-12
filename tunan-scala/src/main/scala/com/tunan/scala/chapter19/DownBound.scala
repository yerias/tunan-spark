package com.tunan.scala.chapter19

class PersonMan(val name:String){
    override def toString: String = name
}

class PoliceMan(override val name: String) extends PersonMan(name){
    override def toString: String = name
}

class SuperMan(override val name: String) extends PoliceMan(name){
    override def toString: String = name
}

object DownBound {

    // 下界 >:
    def printlnArr[T >: PoliceMan](array: Array[T]): Unit = array.foreach(x => println(x.toString))

    def main(args: Array[String]): Unit = {

        val arr1 = Array(new SuperMan("z3"),new SuperMan("l4"),new SuperMan("w5"))
        val arr2 = Array(new PersonMan("z3"),new PersonMan("l4"),new PersonMan("w5"))

//        printlnArr(arr1)
        printlnArr(arr2)

    }
}
