package com.tunan.java.io.test;

import com.tunan.java.io.util.Directory;
import com.tunan.java.io.util.Println;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DirectoryTest {


    /**
     * 全部目录
     */
    @Test
    public void walk() {
        Println.println(Directory.walk(".").dirs,true);
    }

    /**
     * p开头的文件或者目录
     */
    @Test
    public void load() {
        for (File file : Directory.load(".", "p.*")) {
            System.out.println(file);
        }
    }

    /**
     * T开头的java文件
     */
    @Test
    public void walk2() {

        for (File file : Directory.walk(".", "T.*\\.java")) {
            System.out.println(file);
        }

    }

    /**
     * Z to z 的文件
     */
    @Test
    public void walk3() {

        for (File file : Directory.walk(".", ".*\\[zZ\\].*\\.java")) {
            System.out.println(file);
        }

    }

}
