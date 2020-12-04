package com.tunan.scala.chapter21

// 封装特殊类型
class name2string(val name:String)

class ImplicitParameter {

    def maxListPoorStyle[T](elements:List[T])(implicit ordering: Ordering[T]):T = {
        elements match {
            case List() => throw new IllegalArgumentException("empty list")
            case List(x) => x
            case x :: rest =>
                val maxRest = maxListPoorStyle(rest)(ordering)
                if(ordering.gt(x,maxRest)) x else maxRest
        }
    }

    // 需要调用的方法
    def person(implicit name:name2string): String = name.name

    def sqrt(implicit x:Double): Double = Math.sqrt(x)
}

object ImplicitParameter{
    def main(args: Array[String]): Unit = {
        val parameter = new ImplicitParameter
         val result1 = parameter.maxListPoorStyle(List(1, 5, 10, 3))
         val result2 = parameter.maxListPoorStyle(List(1.5, 5.2, 10.7, 3.14159))
         val result3 = parameter.maxListPoorStyle(List("one", "two", "three"))
//        println(result3)

        // 隐式参数，用来对参数进行转换
        implicit val x: name2string = new name2string("zhangsan")
        val parameter1 = new ImplicitParameter
//        println(parameter1.person)

        implicit val a:Double = 2.55
        val parameter2 = new ImplicitParameter
        println(parameter2.sqrt)

    }
}