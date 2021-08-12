package com.tunan.java.sort;

import java.util.Comparator;

/**
 * 外部比较器：Comparator
 */
public class ComparatorStudent {

    private String name;
    private int age;

    public ComparatorStudent() {
    }

    public ComparatorStudent(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "ComparatorStudent{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
