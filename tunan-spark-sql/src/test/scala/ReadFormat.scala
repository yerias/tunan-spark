import org.apache.spark.sql.SparkSession

object ReadFormat {

    def main(args: Array[String]): Unit = {
            val spark = SparkSession
                  .builder()
                  .master("local[2]")
                  .appName(this.getClass.getSimpleName)
                  .getOrCreate()

        val fileDf = spark.read.format("parquet").option("compile", "snappy").load("tunan-spark-sql/extds/etl-access/part-*")

        fileDf.show()
    }
}
