package com.tunan.java.io.stream;

import java.io.*;

/**
 *
 * 基本的文件输出，缓冲流读取文件字节
 *
 */

public class BasicFileOutputByLineNumber {

    static String file = "tunan-java/out/BasicFileOutputByLineNumber.txt";

    public static void main(String[] args) {

        try {
            LineNumberReader in = new LineNumberReader(new FileReader(new File("C:/Users/Administrator/Desktop/a.txt")));

            PrintWriter out = new PrintWriter(file);

            String s;
            while((s = in.readLine()) != null){
                out.println(in.getLineNumber() + ": "+s);
            }

            out.close();
            in.close();

//            System.out.println(BufferedInputFile.read(file));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
