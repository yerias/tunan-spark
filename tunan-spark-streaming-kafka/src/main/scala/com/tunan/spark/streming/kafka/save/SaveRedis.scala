package com.tunan.spark.streming.kafka.save

import com.tunan.spark.utils.mysql.MySQLUtils
import com.tunan.spark.utils.redis.RedisUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SaveRedis {

  def main(args: Array[String]): Unit = {

    // 创建ssc
    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val ssc = new StreamingContext(conf, Seconds(5))

    // 连接kafka配置参数
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop:9090,hadoop:9091,hadoop:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "earliest", //latest
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    // 可以设置多个topic
    val topics = Array("test")
    // 创建DirectStream
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    // 业务逻辑
    stream.map(x => (x.value(),1)).reduceByKey(_+_)
        .foreachRDD(rdd => {
          // 分区操作
          rdd.foreachPartition(partition => {
            // 一个分区连一个jedis
            val jedis = RedisUtils.getJedis
            partition.foreach(fields =>{
              // 保存到Hash中
              jedis.hincrBy("wc_redis",fields._1,fields._2)
            })
            // 关闭连接
            jedis.close()
          })
        })
    // 启动程序
    ssc.start()
    ssc.awaitTermination()
  }
}
