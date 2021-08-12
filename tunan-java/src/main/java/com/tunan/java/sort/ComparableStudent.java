package com.tunan.java.sort;

/**
 * 内部比较器：Comparable
 */
public class ComparableStudent implements Comparable<ComparableStudent>{

    private String name;
    private int age;

    public ComparableStudent() {
    }

    public ComparableStudent(String name, int age) {
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
    public int compareTo(ComparableStudent o) {
        // 负数小于
        // 0 等于
        // 正数大于
        // 升序排序
        return this.age-o.age;

        // 降序排序
//        return o.age-this.age;

    }



    @Override
    public String toString() {
        return "ComparableStudent{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
