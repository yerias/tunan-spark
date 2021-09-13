package com.tunan.java.reflect.test;


/*
 * 通过Class对象可以获取某个类中的：构造方法、成员变量、成员方法；并访问成员；
 *
 * 1.获取构造方法：
 * 		1).批量的方法：
 * 			public Constructor[] getConstructors()：所有"公有的"构造方法
 *          public Constructor[] getDeclaredConstructors()：获取所有的构造方法(包括私有、受保护、默认、公有)
 *
 * 		2).获取单个的方法，并调用：
 * 			public Constructor getConstructor(Class... parameterTypes):获取单个的"公有的"构造方法：
 * 			public Constructor getDeclaredConstructor(Class... parameterTypes):获取"某个构造方法"可以是私有的，或受保护、默认、公有；
 *
 * 		3).调用构造方法：
 *          newInstance是 Constructor类的方法（管理构造函数的类）
 *          api的解释为：
 *               newInstance(Object... initargs)
 *          使用此 Constructor 对象表示的构造方法来创建该构造方法的声明类的新实例，并用指定的初始化参数初始化该实例。
 *          它的返回值是T类型，所以newInstance是创建了一个构造方法的声明类的新实例对象。并为之调用
 * 			    Constructor-->newInstance(Object... initargs)
 */

import com.tunan.java.reflect.student.Constructors;

import java.lang.reflect.Constructor;

public class ConstructorsTest {

    public static void main(String[] args) throws Exception {

        //1.加载Class对象
        Class<?> clazz = Class.forName("com.tunan.java.reflect.student.Constructors");

        //2.获取所有公有构造方法
        System.out.println("\n所有公有构造方法:");
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor);
        }

        //3.获取所有构造方法
        System.out.println("\n所有构造方法:");
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }

        //4.获取公有、无参的构造方法
        System.out.println("\n获取公有、无参的构造方法:");
        Constructor<?> constructor = clazz.getConstructor(null);

        //4.1.因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
        //4.2.返回的是描述这个无参构造函数的类对象。
        System.out.println(constructor);
        Object object = constructor.newInstance();

        // 判断是否可以强转
        if(object instanceof Constructors){
            Constructors cons = (Constructors) object;
            System.out.println(cons);
        }

        System.out.println("\n获取私有构造方法，并调用:");
        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(char.class);
        System.out.println(declaredConstructor);
        //调用构造方法
        declaredConstructor.setAccessible(true);//暴力访问(忽略掉访问修饰符)
        declaredConstructor.newInstance('男');

    }
}