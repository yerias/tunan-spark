package com.tunan.java.reflect.test;

import java.lang.reflect.Method;

public class MainTest {

    public static void main(String[] args) throws Exception {

        //1. 获取Main对象的字节码
        Class<?> clazz = Class.forName("com.tunan.java.reflect.student.Main");

        //2. 获取main方法
        Method main = clazz.getMethod("main", String[].class);

//        Object object = clazz.getConstructor().newInstance();

        // 第一个参数，对象类型，因为方法是static静态的，所以为null可以，
        // 第二个参数是String数组，这里要注意在jdk1.4时是数组，jdk1.5之后是可变参数
        main.invoke(null,(Object)new String[]{"a","b","c"});

    }
}
