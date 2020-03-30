package com.tunan.spark.sortmodule

import com.tunan.spark.utils.ContextUtils
import org.apache.spark.rdd.RDD
import com.tunan.spark.utils.ImplicitAspect.rdd2RichRDD

object SortApp02 {

    def main(args: Array[String]): Unit = {
        val sc = ContextUtils.getSparkContext(SortApp02.getClass.getSimpleName)

        //TODO List中每个东西的含义：名称name、价格price、库存amount
        val rdd = sc.parallelize(List("西瓜 20 100", "苹果 10 500", "香蕉 10 30", "菠萝 30 200"))

        //TODO 面向对象的方式实现排序
        val productRDD: RDD[ProductsInfo] = rdd.map(x => {
            val split = x.split(" ")
            val name = split(0)
            val price = split(1).toDouble
            val amount = split(2).toInt
            new ProductsInfo(name, price, amount)
        })


        // 隐式方法/转换 ==> 偷偷的增强方法
/*        implicit def ProductsInfo2Ordered(productsInfo:ProductsInfo):Ordered[ProductsInfo] = new Ordered[ProductsInfo] {
            println("===========我是隐式方法===============")
            override def compare(that: ProductsInfo): Int = {
                if (that.price-productsInfo.price>0){
                    1
                }else if (that.price-productsInfo.price==0 && that.amount-productsInfo.amount >0){
                    1
                }else{
                    -1
                }
            }
        }*/

        // 隐式变量
/*        implicit val ProductsInfo2Orderding:Ordering[ProductsInfo] = new Ordering[ProductsInfo] {
            println("===========我是隐式变量===============")
            override def compare(x: ProductsInfo, y: ProductsInfo): Int = {
                y.amount - x.amount
            }
        }*/

        // 隐式类
        implicit object ProductsInfo22Orderding extends Ordering[ProductsInfo]{
            //println("===========我是隐式object===============")
            override def compare(x: ProductsInfo, y: ProductsInfo): Int = {
                y.amount - x.amount
            }
        }

        //到底谁的优先级高
        productRDD.sortBy(x => x).print()
        sc.stop()
    }
}



//一个最普通的类,不实现ordered，实现排序的功能
// TODO 这个类没有排序规则 我们通过隐式转换加上去就行了
class ProductsInfo(val name: String, val price: Double, val amount: Int) extends Serializable {
    override def toString: String = s"ProductsInfo:  $name | $price | $amount"
}
