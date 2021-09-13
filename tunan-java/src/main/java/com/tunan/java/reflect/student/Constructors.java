package com.tunan.java.reflect.student;

public class Constructors {

    //（默认的构造方法） default
    Constructors(String name) {
        System.out.println("默认的构造方法 "+name);
    }

    //无参构造方法
    public Constructors() {
        System.out.println("调用了公有、无参的构造方法");
    }

    //有一个参数的构造方法
    public Constructors(char name){
        System.out.println("姓名：" + name);
    }

    //有多个参数的构造方法
    public Constructors(String name ,int age){
        //这的执行效率有问题，以后解决。
        System.out.println("姓名："+name+"年龄："+ age);
    }

    //受保护的构造方法
    protected Constructors(boolean n){
        System.out.println("受保护的构造方法 n = " + n);
    }

    //私有构造方法
    private Constructors(int age){
        System.out.println("私有的构造方法   年龄："+ age);
    }

    @Override
    public String toString() {
        return "调用了实例 Constructors";
    }
}
