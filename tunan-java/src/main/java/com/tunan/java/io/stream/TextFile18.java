package com.tunan.java.io.stream;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * 使用ArrayList实现文件的读写
 */
public class TextFile18 extends ArrayList<String> {

    public TextFile18(String filename, String splitter) throws IOException {
        super(Arrays.asList(read(filename).split(splitter)));

        if ("".equals(get(0))) {
            remove(0);
        }
    }

    public TextFile18(String filename) throws IOException {
        this(filename, "\n");
    }


    public static String read(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        // new FileReader(new File(filename))
        // => new InputStreamReader(new FileInputStream(new File(filename)))
        BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
        String line = null;
        while ((line = in.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static void write(String filename, String text) throws FileNotFoundException {
        // new PrintWriter(filename)
        // => new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName))
        PrintWriter out = new PrintWriter(filename);
        out.write(text);
        out.close();
    }

    public void write(String filename) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filename);
        try {
            for (String text : this) {
                out.write(text);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        out.close();
    }


    public static void main(String[] args) {

        String inFile = "tunan-java/data/word.txt";
        String outFile = "tunan-java/out/word.txt";
        String text = null;
        try {
            // 通过方法读写文件
//             text = read(inFile);
//             write(outFile,text);

            // 通过构造器读写文件
//            TextFile18 textFile18 = new TextFile18(inFile);
//            textFile18.write(outFile);

            // 对文本内容排序，并且保证唯一
            TreeSet<String> uniqueWord = new TreeSet<>(new TextFile18(inFile));
            // 返回小于指定元素的元素组
            System.out.println(uniqueWord.headSet("zhagsan list"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
