package com.tunan.scala.chapter19

// 父类
class Super()

// 子类
class Sub() extends Super()

// 协变 : 类A和类B之间是父子类关系, Pair[A]和Pair[B]之间也有 父子 类关系.
class Covariant[+T](val name: String)

// 逆变 : 类A和类B之间是父子类关系, 但是Pair[A]和Pair[B]之间是 子父 类关系.
class Contravariant[-T](val name: String)

// 非变 : 类A和类B之间是父子类关系, 但是Pair[A]和Pair[B]之间 没有任何 关系 .
class Invariant[T]()

object TypeChange {

    def main(args: Array[String]): Unit = {
        val invSub = new Invariant[Sub]()
//        val invSuper: Invariant[Super] = invSub // 编译报错 Sub和Super是父子关系，但是Invariant[Sub]和Invariant[Super]没关系


        val covSub = new Covariant[Sub]("zs")
        val covSuper: Covariant[Super] = covSub
        println(covSuper.name) //zs

        val conSub = new Contravariant[Sub]("zs")
//        val conSuper: Contravariant[Super] = conSub // Super是Sub是父类，逆变后Contravariant[Super]是Contravariant[Sub]的子类

        val conSub2 = new Contravariant[Super]("zs")
        val conSuper2: Contravariant[Sub] = conSub2
        println(conSuper2.name) // zs

    }
}
