package com.tunan.java.io.stream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 格式化的内存输入 一次一个字节
 */
public class FormattedMemoryInput {

    public static void main(String[] args) {

        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(BufferedInputFile.read("C:/Users/Administrator/Desktop/a.txt").getBytes()));

            while(dis.available() != 0){
                System.out.println((char)dis.readByte());
            }

        } catch (IOException e) {
            System.out.println("结束流");;
        }
    }
}
