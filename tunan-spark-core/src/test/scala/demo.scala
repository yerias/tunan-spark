import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer
import scala.util.Random

object demo {

  /*def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("demo")
    val sc = new SparkContext(conf)
    val value = sc.parallelize(List(1, 2, 3, 4, 5))
    value.foreach(println)
  }*/
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("GenerateSkewdata")
      .setMaster("local[2]")

    val sc = new SparkContext(sparkConf)

    val list = List("kobekun","kobekun",
      "kobekun","kobekun","kobekun","kobekun","kobekun","kobekun","kobekun","kobekun",
      "kobekun","kobekun","kobekun","kobekun","kobekun","kobekun","hello","world")

    val random = new Random()



    val listBuffer = ListBuffer[String]()
    for(x <- 1 to 1000){
      var word = list(random.nextInt(list.size))
      listBuffer.append(word)
    }

    val rdd = sc.parallelize(listBuffer)
    val out = "out"

    rdd.saveAsTextFile(out)

    sc.stop()
  }
}
