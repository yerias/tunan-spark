package com.tunan.spark.sql.join

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object JdbcJoinHive {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().master("local[2]").appName(this.getClass.getSimpleName).enableHiveSupport().getOrCreate()
        import spark.implicits._

        val jdbcDF = spark.read
          .format("jdbc")
          .option("url", "jdbc:mysql://hadoop/?characterEncoding=utf-8&useSSL=false")
          .option("dbtable", "tunan.dept")
          .option("user", "root")
          .option("password", "root")
          .load()

        val hiveDF = spark.sql("select * from default.emp")

        val joinDF: DataFrame = jdbcDF.join(hiveDF, "deptno")


        val selectDF: DataFrame = joinDF.select("ename")

        spark.sql("cache table dept")

        //        selectDF.persist()

        //        joinDF.show(false)

        val joinDS: Dataset[EmpDept] = joinDF.as[EmpDept]
        val selectDS: Dataset[String] = joinDS.map(_.ename)
        //        selectDS.persist()
        //        println(selectDF.queryExecution.optimizedPlan.numberedTreeString)
        //        println("-------------")
        //        println(selectDS.queryExecution.optimizedPlan.numberedTreeString)


        val mapDS = joinDS.map(x => Result(x.empno, x.ename, x.deptno, x.dname, x.prize))
        //        mapDS.show()


        //mapDS.write.format("orc").save("tunan-spark-sql/out")
        /*        mapDS.write.format("jdbc")
                    .option("url", "jdbc:mysql://hadoop/?characterEncoding=utf-8&useSSL=false")
                    .option("dbtable", "tunan.join_result")
                    .option("user", "root")
                    .option("password", "root")
                    .mode("overwrite")
                    .save()*/
    }

    case class EmpDept(deptno: String, dname: String, level: String, empno: String, ename: String, job: String, jno: String, date: String, sal: Double, prize: String)

    case class Result(empno: String, ename: String, deptno: String, dname: String, prize: String)

}
