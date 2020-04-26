package com.tunan.spark.sql.analysis

import org.apache.spark.sql.{DataFrame, SparkSession}

object GroupTopN {

    val in = "tunan-spark-sql/data/access.txt"
    val out = "tunan-spark-sql/out"

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .config("spark.some.config.option", "some-value")
            .getOrCreate()


        val ds = spark.read.textFile(in)

        import spark.implicits._
        //为生成需要的表格做准备
        val df: DataFrame = ds.map(row => {
            val words = row.split(",")
            (words(3), words(12), words(15).toLong)
        }).toDF("country", "domain", "traffic")

        df.createOrReplaceTempView("access")

        // 每个国家的域名流量前2
        val topNSQL="""select
                      |	*
                      |from (
                      |		select
                      |			t.*,row_number() over(partition by country order by sum_traffic desc) r
                      |		from
                      |			(
                      |				select country,domain,sum(traffic) as sum_traffic from access group by country,domain
                      |			) t
                      |	) rt
                      |where rt.r <=2 """.stripMargin

        //spark.sql(topNSQL).show()

        //traffic降序排序
        import org.apache.spark.sql.functions._
        df.groupBy("country","domain").agg(sum("traffic").as("sum_traffic")).sort($"sum_traffic".desc).show()
    }
}
