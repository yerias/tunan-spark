package com.tunan.scala.chapter21

class PreferredPrompt(val preference: String)
class PreferredDrink(val preference: String)

object Greeter {
    def greet(name: String)(implicit prompt: PreferredPrompt,drink: PreferredDrink): Unit = {
        println(s"Welcome, ${name}. The system is ready.")
        print("But while you work,")
        println(s"why not enjoy a cpu of ${drink.preference}?")
        println(prompt.preference)
    }
}

object JoesPrefs{
    implicit val prompt = new PreferredPrompt("Yes,master>")
    implicit val drink = new PreferredDrink("tea")
}


object test{
    def main(args: Array[String]): Unit = {

        import JoesPrefs._
        Greeter.greet("hello")

    }
}