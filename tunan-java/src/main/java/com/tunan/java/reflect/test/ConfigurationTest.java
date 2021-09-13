package com.tunan.java.reflect.test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class ConfigurationTest {

    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName(getProperties("className"));
            // 空参
            Method method = clazz.getMethod(getProperties("methodName"));
            method.invoke(clazz.getConstructor().newInstance());

            // 一个空参
            method = clazz.getMethod(getProperties("methodName"), String.class);
            method.invoke(clazz.getConstructor().newInstance(), "zhangsan");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //此方法接收一个key，在配置文件中获取相应的value
    public static String getProperties(String key) throws IOException {
        //获取配置文件的对象
        Properties pro = new Properties();
        //获取输入流
        FileReader reader = new FileReader("tunan-java/src/main/resources/pro.txt");
        //将流加载到配置文件对象中
        pro.load(reader);
        reader.close();
        //返回根据key获取的value值
        return pro.getProperty(key);
    }
}
