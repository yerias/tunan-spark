package com.tunan.java.io.stream;

import java.io.*;

public class BinaryFile {

    /**
     * 返回指定文件的内容，以字节的形式
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static byte[] read(File file) throws FileNotFoundException {
        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file));

        byte[] data = null;
        try {
            data = new byte[bi.available()];
            bi.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static byte[] read(String file) throws FileNotFoundException {
        return read(new File(file).getAbsoluteFile());
    }

    public static void main(String[] args) {

        try {
            System.out.println(new String(read("C:/Users/Administrator/Desktop/a.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
