package com.tunan.spark.multipledirectory

import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat

class MyMultipleTextOutputFormat extends MultipleTextOutputFormat[Any,Any] {
    //生成最终生成的key的类型，这里不要，给Null
    override def generateActualKey(key: Any, value: Any): Any = NullWritable.get()

    //生成最终生成的value的类型，这里是String
    override def generateActualValue(key: Any, value: Any): Any = {
        value.asInstanceOf[String]
    }

    //生成文件名
    override def generateFileNameForKeyValue(key: Any, value: Any, name: String): String = {
        s"$key/$name"
    }
}
