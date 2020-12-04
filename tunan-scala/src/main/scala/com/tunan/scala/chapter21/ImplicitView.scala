package com.tunan.scala.chapter21


class ImplicitView{
    // 需要传入一个String类型的
    def foo(msg:String): String = msg
}

object ImplicitView {

    def main(args: Array[String]): Unit = {

        // 只有Int类型的
        implicit def Int2String(x:Int): String = x.toString

        val view = new ImplicitView
        println(view.foo(111))

    }
}

class SwingType{
    def  wantLearned(sw : String) = println("兔子已经学会了"+sw)
}
// 类型转换的隐式，定义在object中
object swimming{
    implicit def learningType(S:AminalType) = new SwingType
}
class AminalType
object AminalType extends  App{
    import com.tunan.scala.chapter21.swimming._
    val rabbit = new AminalType
    // rabbit 没有wantLearned方法，但是调用learningType把自己传入返回一个SwingType
    rabbit.wantLearned("breaststroke")
}