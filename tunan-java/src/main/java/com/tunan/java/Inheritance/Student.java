package com.tunan.java.Inheritance;

public class Student extends Person {

    @Override
    public void hello(){
        System.out.println("hello,Test");
    }

    public static void main(String[] args) {
        new Student().hello();
    }
}
