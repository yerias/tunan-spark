package com.tunan.graphx

import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Auther: 李沅芮
 * @Date: 2022/3/25 11:44
 * @Description:
 */
object idMappingGraphX {

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)

        val sc = new SparkContext(conf)

        // 点的集合
        sc.makeRDD(Seq(
            (1, "1"),
            (2, "2"),
            (6, "6"),
            (9, "9"),
            (113, "113"),
            (16, "16"),
            (21, "21"),
            (44, "44"),
            (158, "158"),
            (5, "5"),
            (7, "7")
        ))

        // 边的集合
        val rdd2 = sc.makeRDD(Seq(
            Edge(1L, 133L, 0),
            Edge(2L, 133L, 0),
            Edge(6L, 133L, 0),
            Edge(9L, 133L, 0),
            Edge(6L, 138L, 0),
            Edge(21L, 138L, 0),
            Edge(44L, 138L, 0),
            Edge(16L, 138L, 0),
            Edge(5L, 158L, 0),
            Edge(7L, 158L, 0)
        ))

//        val graph: Graph[(String, Int), Int] = Graph(rdd1, rdd2)
//
//        val vertices: VertexRDD[VertexId] = graph.connectedComponents().vertices
//
//        vertices.foreach(println)
//
//        val vertexId: RDD[(VertexId, (VertexId, (String, Int)))] = vertices.join(rdd1)
//        vertexId.map{
//            case (user_id, (cmid,(name,age))) => (cmid,List((name,age)))
//        }.reduceByKey(_++_).foreach(println)
    }
}
