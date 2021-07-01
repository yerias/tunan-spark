package com.tunan.java.io.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SortedDirList {

    String path = null;
    String[] list = null;

    public SortedDirList(String path){
        File file = new File(path);

        //1.返回值类型不同：前者为String数组，后者为File对象数组
        //2.数组中元素内容不同：前者为string类型的【文件名】（包含后缀名），后者为File对象类型的【完整路径】
        list = file.list();

        assert list != null;
        Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);
    }

    public String[] list(){
        return list;
    }

    public String[] list(String regex){

        ArrayList<String> matchFile = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        int count = 0;
        for (String f : list) {
            if (pattern.matcher(f).matches()){
                count++;
                matchFile.add(f);
            }
        }

        return matchFile.toArray(new String[count]);
    }

    @Override
    public String toString() {
        return "SortedDirList{" +
                "path='" + path + '\'' +
                ", list=" + Arrays.toString(list) +
                '}';
    }

    public static void main(String[] args) {

        SortedDirList dirList = new SortedDirList(".");
//        String[] list = dirList.list();
//        for (String s : list) {
//            System.out.println(s);
//        }

        String[] list1 = dirList.list(".+\\.iml");
        for (String s : list1) {
            System.out.println(s);
        }


    }
}
