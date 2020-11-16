package com.tunan.spark.streming.kafka.join

import com.google.gson.Gson
import com.tunan.spark.streming.kafka.join.domain.{Order, OrderDetail, OrderInfo}
import com.tunan.spark.utils.redis.RedisUtils
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object DoubleStreamJoin {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[3]").setAppName("JoinAppV1")
        val ssc = new StreamingContext(conf, Seconds(10))

        val orderStream = ssc.socketTextStream("aliyun", 9001).map(x => {
            val lines = x.split(",")
            (lines(0), Order(
                lines(0).trim,
                lines(1).trim
            ))
        })

        val orderInfoStream = ssc.socketTextStream("aliyun", 9002).map(x => {
            val lines = x.split(",")
            (lines(1), OrderInfo(
                lines(0).trim,
                lines(1).trim,
                lines(2).trim,
                lines(3).trim,
                lines(4).trim.toInt
            ))
        })

        //        val gson = new Gson
        //        println(gson.toJson(Order("111", "222")))
        //        println(gson.fromJson("{\"orderId\":\"111\",\"userId\":\"222\"}", classOf[Order]))
        //        orderStream.foreachRDD(rdd => rdd.foreach(println))
        //        orderInfoStream.foreachRDD(rdd => rdd.foreach(println))


        val orderDetailRDD = orderStream.fullOuterJoin(orderInfoStream).mapPartitions(partition => {
            println(" ----------------10s---------------------")
            val jedis = RedisUtils.getJedis
            val gson = new Gson()
            val OrderDetails = partition.map({
                // order:orderInfo => 1:n
                case (orderId, (Some(order), Some(orderInfo))) => {
                    // order加入缓存，设置过期时间
                    jedis.setex(s"order:${orderId}", 30, gson.toJson(order))
                    // 得到join后的结果
                    val orderDetail = OrderDetail().mergeOrder(order).mergeOrderInfo(orderInfo)
                    // 要不要根据订单id去缓存中拿到所有缓存的订单信息(1：n 对应的订单信息可能延迟)
                    // orderInfo可能有多个，都是orderId对应的，如果key相同会覆盖，需要对key做处理 => orderId:orderInfoId
                    import scala.collection.JavaConverters._
                    val keys = jedis.keys(s"orderInfo:${orderId}:*").asScala
                    val orderDetails: mutable.Set[OrderDetail] = keys.map(key => {
                        // 拿到该订单所有的订单信息
                        val orderInfo = gson.fromJson(jedis.get(key), classOf[OrderInfo])
                        // 拿到信息后删除该订单信息，否则数据会重复
                        jedis.del(key)
                        // join
                        OrderDetail().mergeOrder(order).mergeOrderInfo(orderInfo)
                    })
                    orderDetails += orderDetail
                }

                // 1:n  =>  1:1
                case (orderId, (None, Some(orderInfo))) => {
                    // redis中查找order
                    val jedisOrder = jedis.get(s"order:${orderId}")

                    if (jedisOrder != null) {
                        // 找到了进行join
                        val order = gson.fromJson(jedisOrder, classOf[Order])
                        OrderDetail().mergeOrder(order).mergeOrderInfo(orderInfo)
                    } else {
                        // 找不到了缓存到redis，不需要设置过期时间，下次被找到时立即删除
                        jedis.set(s"orderInfo:${orderId}:${orderInfo.infoId}", gson.toJson(orderInfo))
                    }
                }

                // 1:n
                case (orderId, (Some(order), None)) => {
                    // 首先缓存order，给定过期时间
                    jedis.setex(s"order:${orderId}", 30, gson.toJson(order))
                    // redis中查找orderInfo
                    import scala.collection.JavaConverters._
                    val keys = jedis.keys(s"orderInfo:${orderId}:*").asScala
                    val details = keys.map(key => {
                        val orderInfo = gson.fromJson(jedis.get(key), classOf[OrderInfo])
                        // join上了删除orderInfo
                        jedis.del(key)
                        OrderDetail().mergeOrder(order).mergeOrderInfo(orderInfo)
                    })
                    details
                }
            })
            RedisUtils.closeJedis(jedis)
            OrderDetails
        })

        orderDetailRDD.foreachRDD(rdd => rdd.foreach(println))


        ssc.start()
        ssc.awaitTermination()
    }
}
