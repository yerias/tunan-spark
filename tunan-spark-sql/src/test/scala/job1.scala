import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object job1 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()
    import spark.implicits._
    val textRDD = spark.sparkContext.textFile("tunan-spark-sql/data/grade.txt")
    val student = textRDD.map(row => {
      val words = row.toString().split(",")
      Student(words(0).toInt, words(1).toInt, words(2), words(3).toDouble)
    })

    val stuDF = student.toDF()

    stuDF.createOrReplaceTempView("student")



    spark.sql("""select a.stu_id
                |from student a
                |join (
                |select stu_id,grade from student where course = 'chinese'
                |) b
                |where a.grade > b.grade and a.course = 'math'
                |group by a.stu_id""".stripMargin).show()

//    spark.sql("select stu_id,grade from student a where a.course = 'chinese'").show()

  }
  case class Student(id:Int,stu_id:Int,course:String,grade:Double)
}
