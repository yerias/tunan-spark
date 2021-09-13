package com.tunan.java.reflect.test;

/*
 * 获取成员变量并调用：
 *
 * 1.批量的
 * 		1).Field[] getFields():获取所有的"公有字段"
 * 		2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
 * 2.获取单个的：
 * 		1).public Field getField(String fieldName):获取某个"公有的"字段；
 * 		2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
 *
 * 	 设置字段的值：
 * 		Field --> public void set(Object obj,Object value):
 * 					参数说明：
 * 					1.obj:要设置的字段所在的对象；
 * 					2.value:要为字段设置的值；
 *
 */

import com.tunan.java.reflect.student.Fields;

import java.lang.reflect.Field;

public class FieldsTest {

    public static void main(String[] args) throws Exception {

        //1.获取Class对象
        Class clazz = Class.forName("com.tunan.java.reflect.student.Fields");

        //2.获取所有公有的字段
        System.out.println("\n获取所有公有的字段");
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            System.out.println(field);
        }
        //3.获取所有字段
        System.out.println("\n获取所有字段(包括私有、受保护、默认的)");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField);
        }

        //4.获取公有字段,并调用
        System.out.println("\n获取公有字段,并调用");
        //4.1获取字段对象
        Field field = clazz.getField("name");
        System.out.println(field);
        //4.2获取实例对象
        Object object = clazz.getConstructor().newInstance();
        //4.3字段设置到对象里去
        field.set(object,"刘德华");

        Fields f = (Fields) object;
        System.out.println("验证设置成功名称:   "+f.name);

        //5.获取私有字段,并调用
        System.out.println("\n获取私有字段,并调用");
        field = clazz.getDeclaredField("phoneNum");
        System.out.println(field);
        field.setAccessible(true);
        // 第一个参数：要传入设置的对象，第二个参数：要传入实参
        field.set(object,"17673601001");
        System.out.println("验证设置成功号码:   "+f);

    }
}