package com.tunan.java.io.stream;

import com.tunan.java.io.util.Directory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 验证.class文件以十六进制字符 'CAFEBABE' 开头
 */
public class Ex20 {

    public static void main(String[] args) throws FileNotFoundException {

        for (File file : Directory.walk("tunan-java/", ".*\\.class").files) {
            System.out.println(file.getName());
            byte[] ba = BinaryFile.read(file);
            for (int i = 0; i < 4; i++) {
                System.out.print(Integer.toHexString(ba[i] & 0xff).toUpperCase());
            }
            System.out.println();
        }
    }
}