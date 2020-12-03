package com.tunan.scala.chapter6

class ConstructorTest(a: Int, b: Int) {

    val number1 = a
    val number2 = b

    override def toString: String = s"a: ${a} , b: ${b}"

    def add(that: ConstructorTest): ConstructorTest =
        if (this.number1 * that.number2 < this.number1 * this.number2) that else this

    def this(a: Int) {
        this(a, 1)
    }
}



object ConstructorTest{

    def main(args: Array[String]): Unit = {

        val test = new ConstructorTest(1, 2)
        val result: ConstructorTest = test.add(new ConstructorTest(1))
        println(result)

    }
}
