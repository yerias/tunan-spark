package com.tunan.java.ormapping.utils;

import com.tunan.java.ormapping.annotation.TunanBean;
import com.tunan.java.ormapping.annotation.TunanField;

import java.lang.reflect.Field;

public class ReflectionUtils {


    /**
     * 获取最终执行的SQL语句
     */
    public static String getSQL(Class<?> clazz) {

        StringBuffer buffer = new StringBuffer("insert into ");
        String table = getTable(clazz);
        buffer.append(table);
        buffer.append(" (");
        String columns = getColumns(clazz);
        buffer.append(columns);



        return buffer.toString();
    }


    /**
     * 获取字段名
     */
    public static String getColumn(Field field) {
        String columns = "";
        TunanField tunanField = field.getAnnotation(TunanField.class);
        if (null != tunanField) {
            columns = tunanField.value();
        } else {
            columns = field.getName();
        }
        return columns;
    }


    /**
     * 获取所有字段名
     */
    public static String getColumns(Class<?> clazz) {

        StringBuilder buffer = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!"id".equals(field.getName())) {
                //有注解获取注解指定的值，没有注解获取字段名
                String column = getColumn(field);
                buffer.append(column).append(",");
            }
        }
        // 删除最后一个位置的 ','
        buffer.deleteCharAt(buffer.toString().length() - 1).append(") values (");

        for (Field field:fields){
            if(!"id".equals(field.getName())){
                buffer.append("?,");
            }
        }
        buffer.deleteCharAt(buffer.toString().length() - 1).append(")");



        return buffer.toString();
    }


    // 获取表名
    public static String getTable(Class<?> clazz) {
        String table = "";
        TunanBean tunanBean = clazz.getAnnotation(TunanBean.class);
        if (null != tunanBean) {
            // 有注解拿注解最后指定的表名
            table = tunanBean.value();
        } else {
            // 没注解直接拿表名
            table = clazz.getSimpleName().toLowerCase();
        }
        return table;
    }


    public static String getMethod(Field field,String type){
        /**
         * id name age
         * type: get
         * getId getName getAge
         *
         * type: set
         * setId setName setAge
         */
        String fieldName = field.getName();

        String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        return type+name;
    }
}
