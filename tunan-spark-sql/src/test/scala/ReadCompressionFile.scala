import org.apache.spark.sql.{DataFrame, SparkSession}

object ReadCompressionFile {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()


    val fileDF: DataFrame = spark.read.format("parquet").option("compression","lz4").load("tunan-spark-sql/out")

    fileDF.show()
  }
}
