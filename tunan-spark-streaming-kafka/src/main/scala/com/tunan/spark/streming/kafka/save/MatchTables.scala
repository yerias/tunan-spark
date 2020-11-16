package com.tunan.spark.streming.kafka.save

import com.tunan.spark.utils.redis.RedisUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.{Jedis, Pipeline}

object MatchTables {

    def main(args: Array[String]): Unit = {

        val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()
        val ssc = new StreamingContext(spark.sparkContext, Seconds(5))


        val topic: String = "match_table"
        val groupId: String = "matchTable_a_separate_group_id_for_each_stream"
        val bootstrapServers = "hadoop:9090,hadoop:9091,hadoop:9092"

        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> bootstrapServers,
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> groupId,
            "auto.offset.reset" -> "earliest", // earliest 、 latest
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )

        val topics = Array(topic)
        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams)
        )
        stream.foreachRDD(rdd => {
            if (!rdd.isEmpty()) {
                val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

                // orderid,money,catagory(1,2,3,4)
                //            val schema = StructType(
                //                List(
                //                    StructField("orderid", LongType),
                //                    StructField("money", DoubleType),
                //                    StructField("catagory", IntegerType)
                //                )
                //            )
                val tupleRDD: RDD[(Long, Double, Int)] = rdd.map(row => {
                    val words = row.value().split(",")
                    var tuple = (0L, 0.0, 0)
                    if (words.length == 3) {
                        tuple = (words(0).toLong, words(1).toDouble, words(2).toInt)
                    }
                    tuple
                })

                tupleRDD.foreachPartition(partition => {
                    var jedis:Jedis = null
                    var pipeline:Pipeline = null
                    try {
                        jedis = RedisUtils.getJedis
                        pipeline = jedis.pipelined()
                        pipeline.multi()
                        var i: Long = 0
                        partition.foreach(row => {
                            i += 1L
                            val catagory = row._3
                            catagory match {
                                case 1 => pipeline.hset("match_table", 1.toString, s"${row._1},${row._2},${row._3}")
                                case 2 => pipeline.hset("match_table", 2.toString, s"${row._1},${row._2},${row._3}")
                                case 3 => pipeline.hset("match_table", 3.toString, s"${row._1},${row._2},${row._3}")
                                case 4 => pipeline.hset("match_table", 4.toString, s"${row._1},${row._2},${row._3}")
                            }
                            if (i % 10 == 0) {
                                pipeline.exec()
                                pipeline.sync()
                            }
                            println("当前累积数据条数：" + i)
                        })
                        //最终同步一次，刷新剩余的数据
                        pipeline.exec()
                        pipeline.sync()
                    } catch {
                        case e: Exception =>
                            pipeline.discard()
                            e.printStackTrace()
                    } finally {
                        pipeline.close()
                        jedis.close()
                    }
                })

                /*                def matchTables(x:Int,row: (Long, Double, Int),pipeline: Pipeline): Unit ={
                                    pipeline.hset("match_table",x.toString,s"${row._1},${row._2},${row._3}")
                                    pipeline.hmset()
                                }*/
                //            val df: DataFrame = spark.createDataFrame(RowRDD, schema)
                //
                //            df.foreachPartition(partition => {
                //                val jedis = RedisUtils.getJedis
                //                partition.foreach(row => {
                //
                //                })
                //            })
                // some time later, after outputs have completed
                stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
            } else {
                println("正在等数据...")
            }
        })

        ssc.start()
        ssc.awaitTermination()
    }
}
