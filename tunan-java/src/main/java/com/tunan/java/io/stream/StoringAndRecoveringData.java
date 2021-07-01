package com.tunan.java.io.stream;

import java.io.*;

/**
 * 存储和恢复数据 字节
 */
public class StoringAndRecoveringData {

    static String file = "tunan-java/out/data.txt";

    public static void main(String[] args) throws IOException {
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

            out.writeDouble(3.1415926);
            out.writeUTF("that was pi");
            out.writeDouble(1.41413);
            out.writeUTF("Square root of 2");

            out.close();

            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            System.out.println(in.readDouble());
            System.out.println(in.readUTF());
            System.out.println(in.readDouble());
            System.out.println(in.readUTF());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
