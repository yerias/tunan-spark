package com.tunan.spark.wc

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD
object TwoWordCount {

    def main(args: Array[String]): Unit = {
        val sc: SparkContext = ContextUtils.getSparkContext(TwoWordCount.getClass.getSimpleName)

        val words = Array("the","me","that","the","here","me","the","this")

        val rdd: RDD[String] = sc.parallelize(words)

        //TODO 第一种实现wc
        rdd.map((_,1)).reduceByKey(_+_).print()

        //TODO 第二种实现wc
        rdd.map((_,1)).groupByKey().mapValues(_.sum).print()


    }


}
