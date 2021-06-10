package com.tunan.java.io.test;

import java.io.*;

public class FileCopy {

    public static void main(String[] args) {

        BufferedWriter out = null;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/Users/Administrator/Desktop/test.txt"))));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("tunan-java/out/copy.txt"))));

            String line;
            while((line = in.readLine()) != null){
                out.write(line);
                out.newLine();
                out.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
