package com.tunan.java.io.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/***
 * 查找指定目录下的所有文件，并按照字符排序 匿名内部类的方式实现
 */
public class FileList2 {

    public static void main(String[] args) {

        String path = "E:/Java/spark/tunan-spark/tunan-spark-sql/src/main/scala/com/tunan/spark/sql/analysis";

        File file = new File(path);

        String[] list;

        if(args.length == 0){
            list = file.list();
        }else{
            list = file.list(new FilenameFilter() {
                final Pattern pattern = Pattern.compile(args[0]);

                @Override
                public boolean accept(File dir, String name) {
                    return pattern.matcher(name).matches();
                }
            });
        }

        // 读字符串进行排序
        if(null != list){
            Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);
            for (String f : list) {
                System.out.println(f);
            }
        }


    }
}