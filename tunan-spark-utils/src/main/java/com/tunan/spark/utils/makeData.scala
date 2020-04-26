package com.tunan.spark.utils

import java.io.{BufferedOutputStream, BufferedReader, BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import scala.util.Random

object makeData {

    private val R = new Random()
    def main(args: Array[String]): Unit = {

        val array = Array("Almond","Apple","Apricot","Avocado","Betelnut","Blueberry","Bullace","Cantaloupe","Orange","Pitaya","Pomelo","Raspberry")
        var writer:BufferedWriter = null
        for(i <- 0 to 500000*4){
            val line = array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))+"	"+ array(R.nextInt(12))
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("tunan-spark-utils/data/fruit.txt"),true)))
            writer.write(line)
            writer.write("\n")
            println(s"正在产生第${i}条数据")
            writer.flush()
        }
        writer.close()
    }
}
