package com.tunan.java.io.stream;

import scala.reflect.internal.Types;

import java.io.*;
import java.util.*;

/**
 *
 */
public class ReverseLines {

    public static void main(String[] args) {

        if (args.length == 0){
            System.exit(1);
        }

//        System.out.println(reverse(args[0]));
        System.out.println(reverse(args[0],true));

//        System.out.println(reverseAndFind(args));

    }

    public static String reverse(String filename,boolean printLineNumber){
        BufferedReader in = null;
        LinkedList<String> list;
        StringBuilder sb = new StringBuilder();
        int lineNumber = 1;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
            String line;

            list = new LinkedList<>();
            while((line = in.readLine())!= null){
                list.add(line.toUpperCase());
            }

            // 检索但不删除此列表的最后一个元素，如果此列表为空，则返回null
            while (list.peekLast() != null){
                // 检索并删除此列表的最后一个元素，如果此列表为空，则返回null。
                sb.append(lineNumber +": " +list.pollLast()+"\n");
                lineNumber ++ ;
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

        return sb.toString();
    }

    public static String reverse(String filename){
        BufferedReader in = null;
        LinkedList<String> list;
        StringBuilder sb = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
            String line;

            list = new LinkedList<>();
            while((line = in.readLine())!= null){
                list.add(line.toUpperCase());
            }

            // 检索但不删除此列表的最后一个元素，如果此列表为空，则返回null
            while (list.peekLast() != null){
                // 检索并删除此列表的最后一个元素，如果此列表为空，则返回null。
                sb.append(list.pollLast()+"\n");
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

        return sb.toString();
    }


    public static String reverseAndFind(String[] args){

        if(args.length < 2){
            System.out.println("Usage: enter file name\n" +
                    "followed by words to find in lines of that file");
            System.exit(1);
        }


        BufferedReader in = null;
        LinkedList<String> list;
        StringBuilder sb = new StringBuilder();

        List<String> findWords = new ArrayList<>();

        for (String arg : args) {
            findWords.add(arg);
        }

        // 删除第一个参数是文件，拿到剩下的参数
        findWords.remove(0);

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0]))));
            String line;

            list = new LinkedList<>();
            while((line = in.readLine())!= null){
                List<String> lineWords = Arrays.asList(line.split("\\W+"));

                // 此方法的返回类型为boolean ，当Collection对象中不存在公共元素时返回true，否则返回false。
                if(!Collections.disjoint(findWords,lineWords)){
                    list.add(line);
                }
            }

            // 检索但不删除此列表的最后一个元素，如果此列表为空，则返回null
            while (list.peekLast() != null){
                // 检索并删除此列表的最后一个元素，如果此列表为空，则返回null。
                sb.append(list.pollLast()+"\n");
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

        return sb.toString();
    }
}
