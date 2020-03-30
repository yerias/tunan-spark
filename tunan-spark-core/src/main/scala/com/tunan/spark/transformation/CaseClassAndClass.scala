package com.tunan.spark.transformation

import java.io.{ByteArrayOutputStream, ObjectOutputStream}


object CaseClassAndClass {
    class A{

    }
    case class Person(name:String,age:Int)
    /**
     * 	1.初始化的时候可以不用new ，也可以加上，但是普通类必须加上new
     * 	2.默认实现了equals、hashCode方法，普通类需要自己实现equals和hashCode方法
     * 	3.默认是可以序列化的，实现了Serializable，普通类需要自己实现序列化接口
     * 	4.case class 构造函数参数是public的，我们可以直接按访问，普通类可以自定义访问级别
     * 	5.case class默认情况下不能修改属性值，普通类可以通过get和set修改属性值
     * 	6.case class支持模式匹配，普通类不支持
     * 	7.case class默认重写了toString，普通类需要自己重写
     */

    def main(args: Array[String]): Unit = {
        //初始化不用new
        val p1 = Person("图南", 18)

        //默认实现了equals和hashCode方法
        val p2 = Person("图南", 18)
        println(p1 == p2)   //true

        //默认实现了序列化
        val out = new ObjectOutputStream(new ByteArrayOutputStream())
        try {
            val status = out.writeObject(p1)
            println("序列化对象 p1 成功。。。")
        } catch {
            case status => println("序列化 p1 失败。。。")
        }
        val a = new A
        try {
            val status = out.writeObject(a)
            println("序列化对象 a 成功。。。")
        } catch {
            case status => println("序列化 a 失败。。。")
        }
        out.close()

        //case class 默认是public的，可以直接访问
        println(p1.name+" "+ p1.age)

        //不支持修改属性值的内容
//        p1.name = "zhangsan"
//        p1.age = 19;


        //case class 支持模式匹配
        printPerson(Person("xiaoha",18))
        printPerson(Person("tunan",24))
        printPerson(Person("xiaoqi",19))
        printPerson(Person("老王",69))
        printPerson(Person("tunan",38))

        //case class 默认重写了toString
        println("我是p1，我重写了toString："+p1.toString)

    }

    def printPerson(person: Person): Unit ={
        person match {
            case Person(name,18) => println(s"hello,我是$name,我今年18岁")
            case Person("tunan",age) => println(s"hello,我是图南，我今年 $age 岁")
            case Person("xiaoqi",19) => println("hello,我是小七")
            case _ => println("my is who?")
        }
    }
}
