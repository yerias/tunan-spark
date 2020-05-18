package com.tunan.java.ormapping.dao.impl;

import com.tunan.java.ormapping.dao.BaseDAO;
import com.tunan.java.ormapping.utils.DBUtils;
import com.tunan.java.ormapping.utils.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BaseDAOImpl implements BaseDAO {
    /**
     * 把传入的t所包含的各种信息变成SQL
     * insert into tunan_user(name,age,brith_day) values(?,?,?)
     * 信息:
     * 1. 根据t以及t上的注解拿到该对象对应的表名
     * 2. 拿到t对象的所有字段信息：对象的原始字段以及TunanField对应的字段
     * 3. 插入表中的对象t所对应的值
     */
    @Override
    public <T> Serializable save(T t) {

        String sql = ReflectionUtils.getSQL(t.getClass());

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs;

        try {
            connection = DBUtils.getConnection();
            pstmt = connection.prepareStatement(sql,new String[]{"id"});


            int index = 1;

            // t最后的值赋值给?
            Class<?> clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if(!"id".equals(field.getName())){
                    // TODO t里面的getter
                    String methodName = ReflectionUtils.getMethod(field, "get");
                    Method method = t.getClass().getDeclaredMethod(methodName);
                    Object o = method.invoke(t);
                    pstmt.setObject(index++,o);
                }
            }
            int rowCount = pstmt.executeUpdate();
            if (rowCount>0){
                rs = pstmt.getGeneratedKeys();
                rs.next();
                return (Serializable) rs.getObject(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(connection);
        }
        return null;
    }
}
