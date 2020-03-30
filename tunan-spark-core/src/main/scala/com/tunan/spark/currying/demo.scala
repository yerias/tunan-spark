package com.tunan.spark.currying

import com.tunan.spark.transformation.CaseClassAndClass.A

object demo {


    def main(args: Array[String]): Unit = {

        val list = List(1, 2, 3, 4, 5, 6)

        val init: Int = 10
        val i = list.foldLeft[Int](init)((x, y) => {
            println(s"x = $x | y = $y" )
            x + y
        })
        //val i = list.foldLeft[Int](init)(_+_)

        println(i)


        /*

        val factor = 3
        val multiplier = (i: Int) => i * factor*/

        //println(multiplier(3))


        //val result1 = sum1(1)
        //val result2 = result1(2)


        /*        val res = sum2(2)(3)
                println(res)*/
       // method("1")("1")
    }

    def sum1(x: Int): Int => Int = (y: Int) => {
        //println(s"$x  ==>  $y")
        x + y
    }


    def sum2(x: Int)(y: Int): Int = x + y

    //定义为柯里化函数时没有问题的，b的类型可以由a参与得出
    def method(a: String)(b:a.type): Unit = {
        println(s"$a      $b")
    }

    //参数列表的第二个参数，a没有定义，不能参与b的类型推断
   /* def method2(a: Int, b: a.type /*a is not defined in a.B*/) {

    }*/
}

class A {

}