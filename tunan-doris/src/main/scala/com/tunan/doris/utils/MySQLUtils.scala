package com.tunan.doris.utils

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}


object MySQLUtils {

  def getConnection: Connection = {
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql://aliyun:9030/example_db?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false","doris","doris")
  }

  def close(connection:Connection, pstmt:PreparedStatement,rs:ResultSet): Unit = {
    if(null != pstmt) pstmt.close()
    if(null != connection) connection.close()
    if(null != rs) rs.close()
  }
}