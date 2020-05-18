package com.tunna.spark.presto.jdbc

import java.sql.DriverManager

object ConnectionMySQL {

    def main(args: Array[String]): Unit = {

        Class.forName("com.facebook.presto.jdbc.PrestoDriver")



        val url = "jdbc:presto://hadoop:8762/mysql/tunan"
        val connection = DriverManager.getConnection(url,"root",null)

        val sql = "select deptno,dname from mysql.tunan.dept";
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery(sql)

        while(rs.next()){
            val deptno = rs.getString("deptno")
            val dname = rs.getString("dname")
            System.out.println(deptno + "  " + dname)
        }

        rs.close()
        stmt.close()
        connection.close()
    }
}
