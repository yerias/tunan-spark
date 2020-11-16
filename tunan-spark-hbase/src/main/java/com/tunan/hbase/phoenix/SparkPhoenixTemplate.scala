package com.tunan.hbase.phoenix

import org.apache.hadoop.conf.Configuration
import org.apache.phoenix.spark._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * Created by jepson ON 2020/3/29 9:59 AM.
 * 官网: https://www.ruozedata.com
 * B站: https://space.bilibili.com/356836323
 * 腾讯课堂: http://ruoze.ke.qq.com
 */
object SparkPhoenixTemplate {

  def main(args: Array[String]): Unit = {

    val zkUrl = "8.129.77.127:2181"
    val configuration = new Configuration()
    configuration.set("hbase.zookeeper.quorum", zkUrl)

    val spark = SparkSession
      .builder()
      .appName("Ruozedata-Spark-Phoenix")
      .master("local[2]")
      .getOrCreate()

    //1.读取：
    //定义通过phoenix从hbase拿表数据，转成DF；注意指定字段(大写)和支持谓词下推
//    val empDF = spark.sqlContext.phoenixTableAsDataFrame(
//      "RUOZEDATA.EMP",
//      Array("EMPNO", "ENAME", "JOB", "MGR", "HIREDATE", "SAL", "COMM", "DEPTNO"),
//      predicate = Some(
//        """
//          |HIREDATE > TO_DATE('1982-02-20', 'yyyy-MM-dd')
//        """.stripMargin),
//      conf = configuration
//    )
//
//    empDF.show()
//    empDF.createOrReplaceTempView("emp") //注册表 就可以写SQL
//
//    val deptDF = spark.sqlContext.phoenixTableAsDataFrame(
//      "RUOZEDATA.DEPT",
//      Array("DEPTNO", "DNAME", "LOC"),
//      conf = configuration
//    )
//    deptDF.show()
//    deptDF.createOrReplaceTempView("dept") //注册表 就可以写SQL
//
//
//    //2.多张表读取 join 、group等 复杂计算
//    var resultDF = spark.sql(
//      """
//        |select e.empno,e.ename,d.dname,d.loc
//        |from emp e left join dept d on e.deptno=d.deptno
//      """.stripMargin)
//    resultDF.show(false)


    val resultDF: DataFrame = spark.read.option("mode","DROPMALFORMED").json("tunan-spark-hbase/data/test.json")

//    jsonDF.show()



    //3.保存
    resultDF.write
      .format("org.apache.phoenix.spark")
      .mode(SaveMode.Overwrite)
      .option("table", "json")
      .option("zkUrl", zkUrl)
      .save()

    spark.stop()
  }
}
