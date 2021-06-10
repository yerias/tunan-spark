package com.tunan.java.io.stream;

import com.tunan.java.io.util.Println;

import java.io.*;

public class Redirecting {

    public static void main(String[] args) {

        PrintStream console = null;
        BufferedInputStream in = null;
        PrintStream out = null;

        BufferedReader br = null;
        String s;

        try {
            console = System.out;
            in = new BufferedInputStream(new FileInputStream(new File("C:/Users/Administrator/Desktop/a.txt")));
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream("tunan-java/out/out.txt")));
            System.setIn(in);
            System.setOut(out);
            System.setErr(out);

            // System.in 指向的是 C:/Users/Administrator/Desktop/a.txt 文件
            br = new BufferedReader(new InputStreamReader(System.in));
            while((s = br.readLine()) != null){
                System.out.println(s);
            }

            System.setOut(console);

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
