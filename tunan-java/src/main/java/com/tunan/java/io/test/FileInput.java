package com.tunan.java.io.test;

import java.io.*;

public class FileInput {

    public static void main(String[] args) throws IOException {
//        readStream();
        readChar();


    }


    public static void readStream() throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File("C:/Users/Administrator/Desktop/test.txt"));

            byte[] buffer = new byte[1024];
            int len ;
            while((len = in.read(buffer)) != -1){
                System.out.println(new String(buffer,0,len));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(null != in){
                in.close();
            }
        }
    }

    public static void readChar(){
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File("C:/Users/Administrator/Desktop/test.txt")));

            String line;

            while((line = in.readLine()) != null){
                System.out.println(line);
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
