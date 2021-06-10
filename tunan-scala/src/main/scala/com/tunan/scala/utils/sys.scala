package com.tunan.scala.utils

import scala.collection.immutable

import scala.collection.JavaConverters.mapAsScalaMapConverter

/**
 * scala有实现的方法，可以直接调用scala的 sys.env
 *
 */

object sys {

    def env: immutable.Map[String, String] = immutable.Map(System.getenv().asScala.toSeq: _*)


    def main(args: Array[String]): Unit = {
        env.foreach{
            case (k,v) => {

                println(k+" => " +v)
            }
        }
    }
}
