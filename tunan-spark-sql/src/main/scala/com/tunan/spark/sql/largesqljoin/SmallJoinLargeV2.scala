package com.tunan.spark.sql.largesqljoin

import org.apache.spark.HashPartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object SmallJoinLargeV2 {

    def main(args: Array[String]): Unit = {
        val start = System.currentTimeMillis()

        val spark = SparkSession
          .builder()
          .master("local[*]")
          .appName(this.getClass.getSimpleName)
          .getOrCreate()


        //        CheckHDFSOutPath.ifExistsDeletePath(spark.sparkContext.hadoopConfiguration, out)
        /*        val userTable = "tunan-spark-sql/moke2/user_click.txt"
                val productTable = "tunan-spark-sql/moke2/product_category.txt"*/

        val userTable = "/data/large_join/user_click.txt"
        val productTable = "/data/large_join/product_category.txt"


        val userRDD = spark.sparkContext.textFile(userTable)
        val productRDD = spark.sparkContext.textFile(productTable)

        val repUser = userRDD.map(row => {
            val words = row.split(",")
            (words(1), words(0))
        }).partitionBy(new HashPartitioner(10))

        val repProduct: RDD[(String, String)] = productRDD.map(row => {
            val words = row.split(",")
            (words(0), words(1))
        }).partitionBy(new HashPartitioner(10))

        // 分区 分区内hash join
        //        val repUser = userRDD.repartition(10)
        //        val repProduct = productRDD.repartition(10)

        //        val repUser = userRDD.(10)
        //        val repProduct = productRDD.repartition(10)

        val pairRDD: RDD[UserProduct] = repUser.mapPartitions(partition => {
            partition.map( row=> {
                UserProduct(row._1,row._2)
            })
        })

        import spark.implicits._
        val userDF = pairRDD.toDF

        userDF.createOrReplaceTempView("user")
        //        userDF.printSchema()

        val productPairRDD = repProduct.mapPartitions(partition => {
            partition.map(row => {
                ProductCategory(row._1,row._2)
            })
        })
        val productDF = productPairRDD.toDF()
        productDF.createOrReplaceTempView("product")
        //        productDF.printSchema()

//        userDF.printSchema()
//        productDF.printSchema()

        val sql = spark.sql(
            """select
              |	u.userId,p.categoryId
              |from
              |	user u
              |inner join
              |	product p
              |on
              | u.productId=p.productId""".stripMargin)

//        sql.show()

        sql.explain()
        sql.queryExecution.debug.codegen()

        spark.sql("select * from user").show()

        /*        spark.sql("""select
                            |	u.userId,p.productId
                            |from
                            |	user u
                            |left join
                            |	product p
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
