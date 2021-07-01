package com.tunan.java.io.stream;

import java.io.*;


/*
    缓存输入文件
 */
public class BufferedInputFile {

    public static String read(String filename) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(new File(filename)));

        String s;

        StringBuilder sb = new StringBuilder();

        while((s = in.readLine()) != null){
            sb.append(s+"\n");
        }

        in.close();
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            String read = read("C:/Users/Administrator/Desktop/a.txt");
            System.out.println(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
