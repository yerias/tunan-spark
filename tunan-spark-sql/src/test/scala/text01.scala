import org.apache.spark.sql.SparkSession

object text01 {

    def main(args: Array[String]): Unit = {

        val spark = SparkSession.builder().master("local[2]").appName(this.getClass.getSimpleName).enableHiveSupport().getOrCreate()

        spark.read.load("")
    }

}
