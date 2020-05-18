package com.tunan.spark.streming.kafka.wc

import com.tunan.spark.utils.mysql.MySQLUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

object wc_ss_kafka {

  var groupId = "use_a_separate_group_id_for_each_stream"
  var topic = "test"
  var brokers = "hadoop:9090,hadoop:9091,hadoop:9092"

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val ssc = new StreamingContext(conf, Seconds(5))


    if (args.length<3){
      throw new IllegalArgumentException("Usage: com.tunan.spark.streming.kafka.wc.wc_ss_kafka <groupId> <topic> <brokers>")
    }

    groupId = args(0)
    topic = args(1)
    brokers = args(2)

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "earliest", //latest
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array(topic)
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(x=>(x.value(),1)).reduceByKey(_+_).foreachRDD(rdd =>{
      rdd.foreach(println)
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
