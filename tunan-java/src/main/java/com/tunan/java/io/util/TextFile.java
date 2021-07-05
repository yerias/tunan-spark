package com.tunan.java.io.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * 文件读写的实用工具
 */

public class TextFile extends ArrayList<String> {

    public static String read(String filename) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(new File(filename).getAbsoluteFile()));

        try {
            String s;
            while((s = br.readLine()) != null){
                sb.append(s);
                sb.append("\n");
            }
        }catch (Exception exception){
            exception.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 字符串内容写入指定文件
     * @param filename
     * @param text
     */
    public static void write(String filename,String text){
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File(filename).getAbsoluteFile());
            out.println(text);
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            out.close();
        }
    }

    // 把自己写入指定文件
    public void write(String fileName){
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File(fileName).getAbsoluteFile());

            // 遍历 ArrayList 中的元素
            for (String item : this) {
                out.println(item);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    // 自己返回一个List
    public TextFile(String filename, String splitter) throws FileNotFoundException {
        // 调用父类的构造器,传入元素,使用父类ArrayList中的方法存储元素
        super(Arrays.asList(read(filename).split(splitter)));

        // 排除第一个元素为空的情况
        if(get(0).equals("")) {
            remove(0);
        }
    }

    public TextFile(String filename) throws FileNotFoundException {
        this(filename,"\n");
    }

    public static void main(String[] args) throws FileNotFoundException {

//        String read = read("C:/Users/Administrator/Desktop/a.txt");
//        write("tunan-java/out/test.txt",read);

        // 在构造器中完成自己(ArrayList)的装载
        TextFile file = new TextFile("tunan-java/out/test.txt");
        // 打印自己的所有元素
        file.write("tunan-java/out/text2.txt");

        // TreeSet中传入ArrayList
        TreeSet<String> words = new TreeSet<String>(new TextFile("C:/Users/Administrator/Desktop/a.txt","\\W+"));
        // headSet : 返回小于指定元素的元素组
        System.out.println(words.headSet("zhagsan"));
    }
}