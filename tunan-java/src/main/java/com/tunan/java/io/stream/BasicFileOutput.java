package com.tunan.java.io.stream;

import java.io.*;

/**
 *
 * 基本的文件输出，缓冲流读取文件字节
 *
 */

public class BasicFileOutput {

    static String file = "tunan-java/out/BasicFileOutput.txt";

    public static void main(String[] args) {

        try {
            BufferedReader in = new BufferedReader(new StringReader(BufferedInputFile.read("C:/Users/Administrator/Desktop/a.txt")));

            PrintWriter out = new PrintWriter(file);

            long lineCount = 1;
            String s;
            while((s = in.readLine()) != null){
                out.println(lineCount + ": "+s);
                lineCount+=1;
            }

            out.close();
            in.close();

//            System.out.println(BufferedInputFile.read(file));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
