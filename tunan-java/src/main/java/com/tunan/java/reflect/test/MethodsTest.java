package com.tunan.java.reflect.test;


import java.lang.reflect.Method;

/*
 * 获取成员方法并调用：
 *
 * 1.批量的：
 * 		public Method[] getMethods():获取所有"公有方法"；（包含了父类的方法也包含Object类）
 * 		public Method[] getDeclaredMethods():获取所有的成员方法，包括私有的(不包括继承的)
 * 2.获取单个的：
 * 		public Method getMethod(String name,Class<?>... parameterTypes):
 * 					参数：
 * 						name : 方法名；
 * 						Class ... : 形参的Class类型对象
 * 		public Method getDeclaredMethod(String name,Class<?>... parameterTypes)
 *
 * 	 调用方法：
 * 		Method --> public Object invoke(Object obj,Object... args):
 * 					参数说明：
 * 					obj : 要调用方法的对象；
 * 					args:调用方式时所传递的实参；
 */
public class MethodsTest {

    public static void main(String[] args) throws Exception {

        //1.获取Class对象
        Class<?> clazz = Class.forName("com.tunan.java.reflect.student.Methods");

        //2.获取所有公有方法
        System.out.println("\n获取所有的公有方法");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println(method);
        }

        //3.获取所有的方法,包括私有的
        System.out.println("\n获取所有的方法,包括私有的");
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod);
        }

        //4.获取公有的show1()方法
        System.out.println("\n获取公有的show1()方法");
        //4.1. 获取方法
        Method show1 = clazz.getMethod("show1", String.class);
        System.out.println(show1);
        //4.2. 获取对象
        Object object = clazz.getConstructor().newInstance();
        //4.3. 调用对象的方法
        show1.invoke(object,"刘德华");

        //5.获取私有的show4()方法
        System.out.println("\n获取私有的show4()方法");
        Method show4 = clazz.getDeclaredMethod("show4", int.class);
        System.out.println(show4);
        //5.1. 解除私有限定
        show4.setAccessible(true);
        //5.2. 需要两个参数，一个是要调用的对象（获取有反射），一个是实参
        Object o = show4.invoke(object, 18);
        System.out.println(o);
    }
}
