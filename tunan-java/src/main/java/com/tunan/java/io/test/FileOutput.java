package com.tunan.java.io.test;

import java.io.*;

public class FileOutput {

    public static void main(String[] args) {
//        writeStream();
        writeChar();
    }


    public static void writeStream() {
        FileOutputStream out = null;
        FileInputStream in = null;

        try {

            in = new FileInputStream(new File("C:/Users/Administrator/Desktop/test.txt"));
            out = new FileOutputStream("tunan-java/out/file-out.txt");

            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                out.flush();

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeChar() {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(new File("tunan-java/out/file-out.txt"), true));
            out.write("\nhello,world");
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
