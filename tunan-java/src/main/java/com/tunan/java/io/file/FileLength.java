package com.tunan.java.io.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class FileLength {

    public static void main(String[] args) {

        File file = new File(".");

        File[] files;

        if(args.length == 0){
            files = file.listFiles();
        }else{
            files = file.listFiles(new FilenameFilter() {
                Pattern pattern = Pattern.compile(args[0]);
                @Override
                public boolean accept(File dir, String name) {
                    return pattern.matcher(dir.getPath()).matches();
                }
            });
        }

        Arrays.sort(files);

        long sum = 0;
        for (File f : files) {
            System.out.println(f+": "+f.length());
            sum += f.length();
        }
        System.out.println("总文件大小: "+sum);

    }
}
