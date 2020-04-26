package com.tunan.spark.streming.kafka.savewc

import com.tunan.spark.utils.mysql.MySQLUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}

object Save2MySQL {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val ssc = new StreamingContext(conf, Seconds(5))


    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop:9090,hadoop:9091,hadoop:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "earliest", //latest
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("test")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value))//.map(_._2).countByValue()
        .map(x=>(x._2,1))
        .reduceByKey(_+_)
        .foreachRDD(rdd =>{
          if (!rdd.isEmpty()){
            rdd.foreachPartition(partition =>{
              MySQLUtils.connect()
              partition.foreach(row => {
                val name = row._1
                val cnt = row._2
                MySQLUtils.insert(s"insert into dstream(name,age) values('$name',${cnt})")
              })
              MySQLUtils.close()
            })
          }
        })

    ssc.start()
    ssc.awaitTermination()
  }
}
