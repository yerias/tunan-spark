package com.tunan.java.io.file;

import com.tunan.java.io.util.Directory;
import sun.security.util.Length;
import sun.util.resources.bg.LocaleNames_bg;

import java.io.File;
import java.util.List;

public class TotalFileLength {

    public static void main(String[] args) {

        File file = new File(".");

        List<File> files;

        if(args.length == 0){
            files = Directory.walk(file).files;
        }else{
            files = Directory.walk(file, args[0]).files;
        }

        long sum = 0;
        for (File f : files) {
            System.out.println(f+": "+f.length());
            sum += f.length();
        }
        System.out.println("总文件大小: "+sum);


    }

}
