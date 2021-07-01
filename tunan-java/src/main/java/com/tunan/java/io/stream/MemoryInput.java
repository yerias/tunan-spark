package com.tunan.java.io.stream;

import java.io.IOException;
import java.io.StringReader;

/**
 * 从内存中输入 一次一个字节
 */
public class MemoryInput {

    public static void main(String[] args){
        try {
            StringReader reader = new StringReader(BufferedInputFile.read("C:/Users/Administrator/Desktop/a.txt"));
            int c;

            while((c = reader.read()) != -1){
                System.out.println((char)c);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
