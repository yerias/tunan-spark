package com.tunan.spark.sortmodule

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD

object SortApp01 {

    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(SortApp01.getClass.getSimpleName)

        //TODO List中每个东西的含义：名称name、价格price、库存amount
        val rdd = sc.parallelize(List("西瓜 20 100", "苹果 10 500", "香蕉 10 30", "菠萝 30 200"))

        //TODO 价格降序排序
        //拆分map拿出来 ==> 返回tuple(productData) ==> 排序sortBy
        val mapRDD = rdd.map(x => {
            val split = x.split(" ")
            val name = split(0)
            val price = split(1).toDouble
            val amount = split(2).toInt
            (name, price, amount)
        })
        //mapRDD.sortBy(-_._2).collect().foreach(println);

        //TODO 价格降序排序，价格相同库存降序排序，在sortBy方法中传入一个tuple
        //拆分 ==> 返回tuple(productData) ==> 排序sortBy(x => (-x._2,-x._3))
        val mapRDD2 = rdd.map(x => {
            val split = x.split(" ")
            val name = split(0)
            val price = split(1).toDouble
            val amount = split(2).toInt
            (name, price, amount)
        })
        mapRDD2.sortBy(x=>(-x._2,-x._3)).collect().foreach(println);


        //TODO 面向对象的方式实现
        val productRDD: RDD[ProductCaseClass] = rdd.map(x => {
            val split = x.split(" ")
            val name = split(0)
            val price = split(1).toDouble
            val amount = split(2).toInt
            ProductCaseClass(name, price, amount)
//            new Products(name, price, amount)
        })

        //productRDD.sortBy(x=>x).collect().foreach(println)

        sc.stop()
    }
}

case class ProductCaseClass(name:String,price:Double,amount:Int) extends Ordered[ProductCaseClass]{
    override def toString: String =  s"case class:  $name | $price | $amount"
    override def compare(that: ProductCaseClass): Int = that.price.toInt - this.price.toInt
}

class Products(val name: String, val price: Double, val amount: Int)
    extends Ordered[Products]
    with Serializable {
    override def toString: String = s"$name | $price | $amount"

    override def compare(that: Products): Int = {
        that.price.toInt-this.price.toInt
    }
}


