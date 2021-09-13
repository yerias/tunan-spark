package com.tunan.java.io.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/***
 *  查找指定目录下的所有文件，并按照字符排序
 */
public class FileList {

    public static void main(String[] args) {

        String path = "E:/Java/spark/tunan-spark/tunan-spark-sql/src/main/scala/com/tunan/spark/sql/analysis";

        File file = new File(path);

        String[] list;

        if(args.length == 0){
            list = file.list();
        }else{
            list = file.list(new DirFilter(args[0]));
        }

        // 读字符串进行排序
        Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);

        for (String f : list) {
            System.out.println(f);
        }
    }
    static class DirFilter implements FilenameFilter{

        public Pattern pattern;

        public DirFilter(String regex){
            pattern = Pattern.compile(regex);
        }

        // dir 是文件的路径，name 是文件的名字
        @Override
        public boolean accept(File dir, String name) {
            System.out.println(dir + "   "+name);
            return pattern.matcher(name).matches();
        }
    }
}



