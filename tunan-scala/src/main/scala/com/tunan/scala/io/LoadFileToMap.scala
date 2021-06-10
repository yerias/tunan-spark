package com.tunan.scala.io

import org.apache.spark.SparkException

import java.io.{File, FileInputStream, IOException, InputStreamReader}
import java.nio.charset.StandardCharsets
import java.util.Properties
import scala.collection.Map
class LoadFileToMap {

    def getPropertiesFromFile(filename: String): Map[String, String] = {
        val file = new File(filename)
        require(file.exists(), s"Properties file $file does not exist")
        require(file.isFile(), s"Properties file $file is not a normal file")

        val inReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
        try {
            val properties = new Properties()
            properties.load(inReader)
            import scala.collection.JavaConversions._
            properties.stringPropertyNames()
                .map { k => (k, trimExceptCRLF(properties.getProperty(k))) }
                .toMap

        } catch {
            case e: IOException =>
                throw new SparkException(s"Failed when loading Spark properties from $filename", e)
        } finally {
            inReader.close()
        }
    }

     def trimExceptCRLF(str: String): String = {
        val nonSpaceOrNaturalLineDelimiter: Char => Boolean = { ch =>
            ch > ' ' || ch == '\r' || ch == '\n'
        }

        val firstPos = str.indexWhere(nonSpaceOrNaturalLineDelimiter)
        val lastPos = str.lastIndexWhere(nonSpaceOrNaturalLineDelimiter)
        if (firstPos >= 0 && lastPos >= 0) {
            str.substring(firstPos, lastPos + 1)
        } else {
            ""
        }
    }
}


object LoadFileToMap{
    def main(args: Array[String]): Unit = {

        val util = new LoadFileToMap()
        util.getPropertiesFromFile("tunan-java/conf/properties.txt").foreach{
            case (k,v) => {
                println(k+"  "+v)
            }
        }
    }
}