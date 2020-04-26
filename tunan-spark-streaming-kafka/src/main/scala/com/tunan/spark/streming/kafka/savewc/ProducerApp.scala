package com.tunan.spark.streming.kafka.savewc

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object ProducerApp {

  def main(args: Array[String]): Unit = {
    // 设置配置文件，这些配置文件都是源码中找的
    val props = new Properties()
    props.put("bootstrap.servers", "hadoop:9090,hadoop:9091,hadoop:9092");
    props.put("acks", "all");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    // 创建producer
    val producer = new KafkaProducer[String, String](props)

    // 循环发送数据
    for (i <- 1 to 100){
         val par = i%3 // 数组走的分区
         producer.send(new ProducerRecord[String, String]("test",par,"",Integer.toString(i)));
    }
    // 关闭producer
    producer.close();
  }
}
