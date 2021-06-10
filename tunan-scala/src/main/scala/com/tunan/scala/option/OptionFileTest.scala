package com.tunan.scala.option

import java.io.File


object OptionFileTest {

    val JAVA_HOME: String = null

    def main(args: Array[String]): Unit = {

        val path: String = Option(JAVA_HOME).getOrElse(getDefaultPath(sys.env))

        println(path)


    }

    def getDefaultPath(env:Map[String,String]): String ={
        env.get("java_home").orElse(env.get("JAVA_HOME")).map(t => new File(t))
            .filter(_.isDirectory)
            .map(_.getAbsolutePath)
            .orNull

    }
}
