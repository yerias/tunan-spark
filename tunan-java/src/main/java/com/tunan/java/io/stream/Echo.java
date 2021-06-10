package com.tunan.java.io.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 标准IO
 */
public class Echo {

    public static void main(String[] args) {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(System.in));
            String s;
            while((s = in.readLine()) != null && s.length() != 0){
                System.out.println(s);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
