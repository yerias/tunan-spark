import org.apache.spark.sql.SparkSession

object dsdf {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()



    import spark.implicits._

    Seq(("zhangsan",18,"男")).toDF().show()


    spark.stop()
  }
}
