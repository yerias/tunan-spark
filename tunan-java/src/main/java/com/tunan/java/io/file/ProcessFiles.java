package com.tunan.java.io.file;

import com.tunan.java.io.util.Directory;

import java.io.File;
import java.io.IOException;

/**
 * 策略模式查找指定目录下的特定后缀的文件，可递归查找
 */

public class ProcessFiles {

    // Strategy 策略设计模式
    public interface Strategy{
        void process(File file);
    }

    // 这个参数用来调用接口中的方法，最后用实现的接口处理逻辑
    private Strategy strategy;
    private String ext;

    // 构造方法
    public ProcessFiles(Strategy strategy,String ext){
        this.strategy = strategy;
        this.ext = ext;
    }

    public void start(String[] args){
        try {
            // 如果没有参数，默认当前目录
            if(args.length == 0){
                processDirectoryTree(new File("."));
            }else{
                for (String arg : args) {
                    File file = new File(arg);
                    // 如果参数是一个目录，交给processDirectoryTree处理，递归出所有文件
                    if(file.isDirectory()){
                        processDirectoryTree(file);
                    }else{
                        // 如果参数直接指定的是一个文件，拼接出后缀
                        if(!arg.endsWith("."+ext)){
                            arg += "." + ext;
                            strategy.process(new File(arg).getCanonicalFile());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // 处理指定目录的时候，拿到指定后缀的的所有文件，拿到的文件的逻辑和参数直接指定文件一样
    public void processDirectoryTree(File root) throws IOException {
        for (File file : Directory.walk(root.getAbsolutePath(), ".*\\." + ext).files) {
            strategy.process(file.getCanonicalFile());
        }
    }

    public static void main(String[] args) {
        // 实现process的逻辑
        new ProcessFiles(new Strategy() {
            @Override
            public void process(File file) {
                System.out.println(file);
            }
        },"java").start(args);
    }


}
