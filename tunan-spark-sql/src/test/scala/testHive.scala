import org.apache.spark.sql.SparkSession

object testHive {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
          .builder()
          .master("local[2]")
          .appName(this.getClass.getSimpleName)
          .enableHiveSupport()
          .getOrCreate()

        spark.sql("show databases").show()
    }
}
