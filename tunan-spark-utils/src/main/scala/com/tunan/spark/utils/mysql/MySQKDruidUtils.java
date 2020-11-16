package com.tunan.spark.utils.mysql;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class MySQKDruidUtils {

    private static DataSource ds;

    static {
        try {
            //1.创建配置文件对象
            Properties pro = new Properties();
            //2.加载配置文件
            pro.load(new FileReader("E:\\Java\\spark\\tunan-spark\\tunan-spark-utils\\src\\main\\resources\\druid.properties"));
            //3.创建DataSource 连接池
            ds = DruidDataSourceFactory.createDataSource(pro);
            DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close(ResultSet result, Statement state, Connection conn) {
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertBatch(String sql, ArrayList<User> users) {
        Connection conn = null;
        PreparedStatement state = null;
        try {
            conn = getConnection();
            state = conn.prepareStatement(sql);

            for (User user : users) {
                state.setString(1, user.getName());
                state.setInt(2, user.getAge());
                state.setString(3, user.getBirth_day());
                state.addBatch();

            }
            int[] executeBatch = state.executeBatch();
            for (int i = 0; i < executeBatch.length; i++) {
                System.out.println(i+"access");
            }
        } catch (Exception e) {
            close(null,state,conn);
        }
    }

    public static void main(String[] args) {
        String sql = "replace into tunan_user(name,age,birth_day) values(?,?,?)";

        ArrayList<User> users = new ArrayList<>();
        users.add(new User("zhangsan",15,"2020-09-15"));
        users.add(new User("lisi",16,"2020-09-16"));
        users.add(new User("wangwu",17,"2020-09-17"));

        insertBatch(sql,users);
    }
}
