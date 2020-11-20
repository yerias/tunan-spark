package com.tunan.spark.transformation

import com.tunan.spark.utils.ContextUtils

object transformationdemo001 {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(transformationdemo001.getClass.getSimpleName)

        val listRDD = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9))
        val listRDD2 = sc.parallelize(List(1, 2, 3, 4, 5))
        val listRDD3 = sc.parallelize(List(4, 5, 6, 7, 8, 9, 9, 9, 8, 5))

        val mapRDD = sc.parallelize(List(("zhangsan", 18), ("zhangsan", 22), ("list", 21), ("wangwu", 26)))
        val mapRDD2 = sc.parallelize(List(("zhaoliu", 18), ("zhangsan", 22), ("list", 21), ("wangwu", 26)))
        val mapRDD3 = sc.parallelize(List(("hongqi", "男"), ("zhangsan", "男"), ("list", "女"), ("wangwu", "男")))

        //        (listRDD.take(3).foreach(println))
        //        println(listRDD.top(3)(Ordering.by(x => -x)))
        //        listRDD.top(3)(Ordering.by(x => -x)).foreach(println)
        //        mapRDD2.join(mapRDD3).print()
        //        listRDD.takeOrdered(3)(Ordering.by(x => x)).foreach(println)

        //        println(listRDD.reduce(_ + _))
        //        mapRDD2.cogroup(mapRDD3).print()
        //        listRDD.foreachPartition(x=>x.foreach(println))
        //        mapRDD.countByKey().foreach(println)
        //        mapRDD.lookup("zhangsan").foreach(println)

        //        listRDD3.map(x=>(x,null)).reduceByKey((x,y)=>x).map(_._1).print()
        //        listRDD.glom().collect().foreach(f=>f.foreach(x => println(x+" ")))

        /*        mapRDD.groupByKey().print()
                  mapRDD.groupByKey().mapValues(_.sum).print()*/

        //        listRDD.filter(_>5).print()
        //        listRDD.map((_, 1)).print()
        /*        listRDD.sample(true,0.4).print()

                val name = List("张三", "李四", "王五")
                val age = List(19, 26, 38)
                val zipRDD: List[((String, Int), Int)] = name.zip(age).zipWithIndex


                val ints: List[Int] = list1.union(list2)

                ints.foreach(println)
                val inter: List[Int] = list1.intersect(list2)
                inter.foreach(println)

        //        list1.subtract(list2)
                listRDD2.subtract(listRDD3)

                val car = listRDD2.cartesian(listRDD3)*/
        val list1 = List(1, 2, 3, 4, 5, 6)
        val list2 = List(4, 5, 6, 7, 8, 8, 8)
        /*        val dist = list2.distinct
                dist.foreach(println)*/

        /*        val iterable = dist.map(x => (x, null)).groupBy(_._1).map(_._1)
                iterable.foreach(println)*/
        /*        val dist: RDD[Int] = listRDD3.distinct()
                dist.foreach(println)
                val dist2: RDD[Int] = listRDD3.map(x => (x, null)).reduceByKey((x, y) => x).map(_._1)
                dist2.foreach(print)*/

        /*        val repart = listRDD2.coalesce(1)
                println(repart.getNumPartitions)*/


        //        mapRDD.sortBy(x=>x._2).print()

        //        mapRDD.sortByKey().print()

        //        mapRDD.map(x=>(x._2,x._1)).sortByKey(false).map(x=>(x._2,x._1)).print()
        //        println(mapRDD.groupByKey(3).getNumPartitions)
        //          mapRDD.groupByKey().mapValues(_.sum).print()
        //        mapRDD.reduceByKey(_+_).print()


        /*listRDD.mapPartitions(partition => {
            partition.map((_,1))
        })*/

        /*listRDD.mapPartitionsWithIndex((index,partition) =>{
            partition.map(x => (index,x))
        }).print()*/

        //mapRDD.reduceByKey(_+_).print()

        //mapRDD.mapValues(x=>x).print()

        //        mapRDD.mapValues(x=>x).sum()
    }
}
