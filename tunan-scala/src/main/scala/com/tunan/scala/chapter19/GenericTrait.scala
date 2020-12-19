package com.tunan.scala.chapter19


// 泛型特质指的是 把泛型定义到特质的声明上 ,
// 即:该特质中的成员的参数类型是由泛型来决定的 .
// 在定义泛型特质的子类或者子单例对象时, 明确具体的数据类型.
trait GenericTrait[T] {

    def show(a:T): Unit = println(a)

}


class GenericTraitA[E] extends GenericTrait[E] {
}

class GenericTraitB extends GenericTrait[Int] {

}


object GenericTrait {
    def main(args: Array[String]): Unit = {
        val a = new GenericTraitA[String]
        a.show("zs")

        val b = new GenericTraitB
        b.show(1)

    }

}
