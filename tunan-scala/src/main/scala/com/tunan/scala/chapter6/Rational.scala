package com.tunan.scala.chapter6

class Rational(n: Int, d: Int) {

    // 前置条件检查
    require(d > 0)

    // 抽出传入值的共同属性/特点
    private val g = gcd(n,d)

    // 简化
    val number: Int = n / g
    val demon: Int = d / g

    override def toString: String = s"${number}/${demon}"

    // 两个有理数相加
    def + (that: Rational): Rational = {
        new Rational(this.number * that.demon + this.demon * that.number, this.demon * that.demon)
    }

    def + (i: Int): Rational = {
        new Rational(this.demon  + i * number, this.demon)
    }

    // 两个有理数相减
    def - (that: Rational): Rational = {
        new Rational(this.number * that.demon - this.demon * that.number, this.demon * that.demon)
    }

    def - (i: Int): Rational = {
        new Rational(this.demon  - i * number, this.demon)
    }

    // 两个有理数相乘
    def * (that: Rational): Rational = {
        new Rational(this.number * that.number, this.demon * that.demon)
    }

    def * (i: Int): Rational = {
        new Rational(this.number * i, this.demon)
    }

    // 两个有理数相除
    def / (that: Rational): Rational = {
        new Rational(this.number * that.demon, this.demon * that.number)
    }

    def / (i: Int): Rational = {
        new Rational(this.number, this.demon * i)
    }


    // 小于
    def lessThan(that:Rational): Boolean ={
        this.number * that.demon < that.number * this.demon
    }

    // 最大值
    def max(that:Rational): Rational ={
        if(this.lessThan(that)) that else this
    }

    // 辅助构造器
    def this(n:Int){
        this(n,1)
    }

    // 求最大公约数
    def gcd(a:Int,b:Int): Int ={
        if(b == 0) a else gcd(b,a%b)
    }
}


object Rational {
    def main(args: Array[String]): Unit = {

        // 隐式转换
        implicit def intToRational(x:Int): Rational = new Rational(x)
        val r = new Rational(66,42)
        println(2 * r)

    }
}
