package com.tunan.scala.`implicit`

object ImplicitTest {

    def main(args: Array[String]): Unit = {

        implicit def int2Double(x:Int):Double = x.toDouble

        implicit def double2Int(x:Double):Int = x.toInt

        implicit class RectangleMaker(width:Int){
            def x(height:Int): Rectangle = Rectangle(width,height)
        }



    }
}

case class Rectangle(width:Int,height:Int)


