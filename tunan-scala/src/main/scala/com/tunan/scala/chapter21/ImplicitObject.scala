package com.tunan.scala.chapter21

object ImplicitObject extends App {

    //定义一个trait Multiplicable
    trait Multiplicable[T] {
        def multiply(x: T): T
    }

    //定义一个隐式对象，用于整型数据相乘
    implicit object MultiplicableInt extends Multiplicable[Int] {
        override def multiply(x: Int): Int = x * x
    }

    //定义一个用于字符串相乘的隐式对象
    implicit object MultiplicableString extends Multiplicable[String] {
        override def multiply(x: String): String = x * 2
    }

    //隐式导入Multiplicable，根据T来自动判断类型，调用哪个子类
    def multiplya[T](x: T)(implicit ev: Multiplicable[T]): T = {
        ev.multiply(x)
    }

    println(multiplya(("5")))  // 55
    println(multiplya((5)))     //25
}