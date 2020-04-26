package com.tunan.spark.sql.largesqljoin

import com.tunan.spark.utils.hadoop.CheckHDFSOutPath
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object SmallJoinLarge {

    def main(args: Array[String]): Unit = {
        val start = System.currentTimeMillis()

        val spark = SparkSession
          .builder()
          .master("local[*]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()


//        CheckHDFSOutPath.ifExistsDeletePath(spark.sparkContext.hadoopConfiguration, out)

        import spark.implicits._
        /*        val userTable = "tunan-spark-sql/moke2/user_click.txt"
                val productTable = "tunan-spark-sql/moke2/product_category.txt"*/

        val userTable = "/data/large_join/user_click.txt"
        val productTable = "/data/large_join/product_category.txt"


        val userRDD = spark.sparkContext.textFile(userTable)
        val productRDD = spark.sparkContext.textFile(productTable)

        // 分区 分区内hash join
        val repUser = userRDD.repartition(10)
        val repProduct = productRDD.repartition(10)

        val pairRDD= userRDD.mapPartitions(partition => {
            partition.map(row => {
                val words = row.split(",")
                UserProduct(words(1), words(0))
            })
        })

        val userDF = pairRDD.toDF()
        userDF.createOrReplaceTempView("user")

        //        userDF.printSchema()

        val productPairRDD = productRDD.mapPartitions(partition => {
            partition.map(row => {
                val words = row.split(",")
                ProductCategory(words(0), words(1))
            })
        })
        val productDF = productPairRDD.toDF()
        productDF.createOrReplaceTempView("product")
        //        productDF.printSchema()

        val sql = spark.sql(
            """select
              |	u.userId,p.categoryId
              |from
              |	user u
              |left join
              |	product p
              |on u.productId=p.productId""".stripMargin)
        sql.show()

        /*        spark.sql("""select
                            |	u.userId,p.categoryId
                            |from
                            |	user u
                            |left join
                            |	(select * from product order by productId) p
                            |on u.productId=p.productId""".stripMargin).show()*/

        /*        map(words(0))=words(1)
                spark.udf.register("get_category",(productId:String)=>{
                    map(productId)
                })*/
        val end = System.currentTimeMillis()
        println("程序花费时间：" + (end - start) + "ms")
    }

    case class UserProduct(productId: String, userId: String)

    case class ProductCategory(productId: String, categoryId: String)

}
