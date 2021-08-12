package com.tunan.java.sort;

import java.util.*;

public class Test {
    public static void main(String[] args) {

        List<ComparableStudent> comparableStudents = new ArrayList<>();
        comparableStudents.add(new ComparableStudent("zhangsan",20));
        comparableStudents.add(new ComparableStudent("lisi",18));
        comparableStudents.add(new ComparableStudent("wangwu",25));

        // 内部比较器
        Collections.sort(comparableStudents);
        for (ComparableStudent student : comparableStudents) {
            System.out.println(student);
        }

        List<ComparatorStudent> comparatorStudent = new ArrayList<>();
        comparatorStudent.add(new ComparatorStudent("zhangsan",20));
        comparatorStudent.add(new ComparatorStudent("lisi",18));
        comparatorStudent.add(new ComparatorStudent("wangwu",25));

        // 外部比较器
        Collections.sort(comparatorStudent, new Comparator<ComparatorStudent>() {
            @Override
            public int compare(ComparatorStudent o1, ComparatorStudent o2) {
                return o1.getAge()-o2.getAge();
            }
        });
        for (ComparatorStudent student : comparatorStudent) {
            System.out.println(student);
        }
    }
}
