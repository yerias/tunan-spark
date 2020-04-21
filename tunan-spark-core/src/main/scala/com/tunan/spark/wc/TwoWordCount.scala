package com.tunan.spark.wc

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD
import org.apache.spark.internal.Logging
object TwoWordCount extends Logging{

    def main(args: Array[String]): Unit = {
        val sc: SparkContext = ContextUtils.getSparkContext(this.getClass.getSimpleName)


        val words = Array("the","me","that","the","here","me","the","this")

        val rdd: RDD[String] = sc.parallelize(words)

        //TODO 第一种实现wc
//        rdd.map((_,1)).reduceByKey(_+_).print()

        log.info("这是我的大小老婆")

        //TODO 第二种实现wc
        rdd.map((_,1)).groupByKey().mapValues(_.sum).print()

        sc.stop()
    }


}
