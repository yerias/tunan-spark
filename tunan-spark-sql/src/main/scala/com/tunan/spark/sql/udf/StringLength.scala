package com.tunan.spark.sql.udf

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object StringLength {

    def main(args: Array[String]): Unit = {

        val in = "tunan-spark-sql/data/love.txt"
        val out = "tunan-spark-sql/out"

        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .config("spark.some.config.option", "some-value")
            .getOrCreate()

        //读取文本内容
        val ds: Dataset[String] = spark.read.textFile(in)

        //文本转换成DF
        import spark.implicits._
        val df: DataFrame = ds.map(row => {
            val words = row.split("\t")
            (words(0), words(1))
        }).toDF("name", "love")

        //创建UDF
        spark.udf.register("length", (love: String) => {
            love.split(",").length
        })

        //DF创建临时表
        df.createOrReplaceTempView("udf_love")

        //在sql中使用UDF函数
        spark.sql("select name,love,length(love) as love_length from udf_love").show()

        //使用api的方案解决

        //自定义的udf需要返回值
        val loveLengthUDF: UserDefinedFunction = spark.udf.register("length", (love: String) => {
            love.split(",").length
        })
        df.select($"name",$"love",loveLengthUDF($"love")).show()
    }
}
