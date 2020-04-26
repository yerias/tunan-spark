package com.tunan.spark.sql.dataframe

import org.apache.spark.sql.{DataFrame, SQLContext, SparkSession}

object json2df {

    def main(args: Array[String]): Unit = {

        val in = "tunan-spark-sql/data/people.json"
        val out = "tunan-spark-sql/out"

        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .config("spark.some.config.option", "some-value")
            .getOrCreate()


        val df: DataFrame = spark.read.format("json").load(in)

        // 使用$"" 导入隐式转换
        import spark.implicits._
        //        打印Schema信息
        //        df.printSchema()
        /**
         * 1. 使用select可以选取打印的列，空值为null
         * 2. show()默认打印20条数据，可以指定条数
         * 3. truncate默认为true，截取长度，可以设置为false
         */
        /*// TODO 可以使用UDF
        df.select($"name",$"age").show(2,false)
        // TODO 不可以使用UDF 适合大部分场景
        df.select("name","age").show(2,false)
        // TODO 不推介，写着复杂
        df.select(df("name"),df("age")).show(2,false)*/
//        df.printSchema()


//        df.select(df("name"),df("age")).filter('name === "Andy").show()
//        df.select(df("name"),df("age")).filter(df("name") === "Andy").show()
//        df.select(df("name"),df("age")).filter("name = 'Andy'").show()
        val selectDf: DataFrame = df.select($"name", $"age")
        selectDf.write.format("json").mode("overwrite").save(out)


    }
}
