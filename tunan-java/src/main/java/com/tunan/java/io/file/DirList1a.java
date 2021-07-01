package com.tunan.java.io.file;

import com.tunan.java.io.util.TextFile;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

public class DirList1a {

    public static void main(String[] args) {

        File file = new File("C:/Users/Administrator/Desktop/");

        final String[] list;

        if(args.length < 2){
            list = file.list();
            System.out.println("Usage: enter filtering regex");
            System.out.println(
                    "followed by words, one or more of which each file must contain.");
        }else{
            list = file.list(new FilenameFilter() {
                Pattern pattern = Pattern.compile(args[0]);
                @Override
                public boolean accept(File dir, String name) {
                    return pattern.matcher(name).matches() &&
                            // 判断2个list中是否有相同的数据
                            !(Collections.disjoint(
                            Arrays.asList(args).subList(1,args.length),
                            new TextFile(dir+"/"+name,"\\W+")
                    ));
                }
            });
        }
        Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);
        for (String f : list) {
            System.out.println(f);
        }
    }
}
