package com.tunna.spark.presto.jdbc

import java.sql.{Driver, DriverManager}
import java.util.Properties

object ConnectionHive {

    def main(args: Array[String]): Unit = {

        Class.forName("com.facebook.presto.jdbc.PrestoDriver")

        val url = "jdbc:presto://hadoop:8762/hive/default"
        val connection = DriverManager.getConnection(url,"root",null)

        val sql = "select empno,ename from hive.default.emp";
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery(sql)

        while(rs.next()){
            val empno = rs.getString("empno")
            val ename = rs.getString("ename")
            System.out.println(empno + "  " + ename)
        }

        rs.close()
        stmt.close()
        connection.close()
    }
}
