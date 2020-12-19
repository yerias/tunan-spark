package com.tunan.scala.chapter21

object ImplicitType {
    def main(args: Array[String]): Unit = {

        // 隐式类型转换 ==> 增强
        implicit def child2Student(child: Child):Student222 = new Student222(child.name)

        val child = new Child("zs")

        child.play()

    }
}



class Child(s:String){
    val name = s
}


private class Student222(name:String){
    def play(): Unit = println(s" $name 超人不会飞")
}
