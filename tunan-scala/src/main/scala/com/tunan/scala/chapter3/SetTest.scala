package com.tunan.scala.chapter3

import scala.collection.mutable
import scala.collection.parallel.immutable


object SetTest {

    def main(args: Array[String]): Unit = {

        mutableHashSet()
//        mutableSet()
//        immutableSet()


    }
    // 可变的HashSet
    private def mutableHashSet(): Unit = {
        val set = mutable.HashSet("Hello", "Jerry", "Tom")
        println(set.hashCode())
        set.+=("Apple")
        println(set.hashCode())
        println(set.contains("Apple"))
    }
    // 可变的Set
    private def mutableSet(): Unit = {
        val set = mutable.Set("Hello", "Jerry", "Tom")
        println(set)
        set += "Apple"
        println(set.hashCode())
        println(set.contains("Apple"))
    }
    // 不可变的Set
    private def immutableSet(): Unit = {
        val set = Set("Hello", "Jerry", "Tom")
        println(set.hashCode())
        val newSet = set + "Apple"
        println(newSet.hashCode())
        println(newSet.contains("Apple"))
    }
}
