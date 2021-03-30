package com.tunan.spark.sql.extds.hbase

import org.apache.spark.sql.SparkSession

object SparkHBaseSourceApp {

    // 主类
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName(this.getClass.getSimpleName)
            .getOrCreate()

        val dept = spark.read.format("com.tunan.spark.sql.extds.hbase")
            .option("hbase.table.name", "dept")
            .option("spark.table.schema", "(deptno:string,dname:string,loc:string)")
            .option("spark.zookeeper.host.port", "aliyun:2181")
            .option("spark.select.cf", "info")
//            .option("spark.row.range", "7844->7902")
            .load()

//        dept.printSchema()
//        dept.show()

        val emp = spark.read.format("com.tunan.spark.sql.extds.hbase")
            .option("hbase.table.name", "emp")
            .option("spark.table.schema", "(key:string,empno:string,ename:string,job:string,mgr:string,hiredate:string,sal:double,comm:double,deptno:string)")
            .option("spark.zookeeper.host.port", "aliyun:2181")
            .option("spark.select.cf", "info")
            .load()

        dept.createOrReplaceTempView("dept")
        emp.createOrReplaceTempView("emp")

//        emp.show()

        spark.sql(
            """
              |SELECT
              | a.empno,a.ename,a.job,a.mgr,a.hiredate,a.sal,a.comm,b.dname,b.loc
              |FROM
              | dept b
              |JOIN
              | emp a
              |ON a.deptno = b.deptno
              |""".stripMargin).show()

        spark.stop()

    }
}
