package com.tunan.scala.chapter21

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
}

object ImplicitParameter{
    def main(args: Array[String]): Unit = {
         val parameter = new ImplicitParameter
         val result1 = parameter.maxListPoorStyle(List(1, 5, 10, 3))
         val result2 = parameter.maxListPoorStyle(List(1.5, 5.2, 10.7, 3.14159))
         val result3 = parameter.maxListPoorStyle(List("one", "two", "three"))
        println(result3)
    }
}
