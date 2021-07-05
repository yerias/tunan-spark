package com.tunan.java.io.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 查找指定目录，指定匹配模式的所有文件和目录，可递归
 */
public class Directory {

    public static File[] load(File dir, final String regex) {

        return dir.listFiles(new FilenameFilter() {
            final Pattern pattern = Pattern.compile(regex);

            @Override
            public boolean accept(File dir, String name) {
                // new File(name).getName() == name
                return pattern.matcher(new File(name).getName()).matches();
            }
        });
    }

    public static File[] load(String dir, final String regex) {

        return load(new File(dir), regex);
    }

    public static class TreeInfo implements Iterable<File> {

        // 存储所有文件
        public List<File> files = new ArrayList<File>();
        // 存储所有目录
        public List<File> dirs = new ArrayList<File>();

        @Override
        public Iterator<File> iterator() {
            return files.iterator();
        }

        /**
         * files 加入其它TreeInfo的files
         * dirs  加入其它TreeInfo的dirs
         *
         * @param other
         */
        void addAll(TreeInfo other) {
            files.addAll(other.files);
            dirs.addAll(other.dirs);
        }

        @Override
        public String toString() {
            return "files=" + Println.println(files) + ", dirs=" + Println.println(dirs);
        }
    }

    /**
     * 读取在指定目录和过滤条件
     *
     * @param start
     * @param regex
     * @return
     */
    public static TreeInfo walk(String start, String regex) {
        return recurseDirs(new File(start), regex);
    }

    /**
     * 读取在指定目录和过滤条件
     *
     * @param start
     * @param regex
     * @return
     */
    public static TreeInfo walk(File start, String regex) {
        return recurseDirs(start, regex);
    }

    /**
     * 读取在指定目录
     *
     * @param start
     * @return
     */
    public static TreeInfo walk(File start) {
        return recurseDirs(start, ".*");
    }

    /**
     * 读取在指定目录
     *
     * @param start
     * @return
     */
    public static TreeInfo walk(String start) {
        return recurseDirs(new File(start), ".*");
    }

    /**
     * 递归读取目录，每次递归创建一个TreeInfo，存储当前目录下的文件和目录，
     * 递归结束的时候调动前一个TreeInfo的addAll方法，把当前的数据存储到前一个TreeInfo中去
     *
     * @param startDir
     * @param regex
     * @return
     */
    static TreeInfo recurseDirs(File startDir, String regex) {
        TreeInfo result = new TreeInfo();
        for (File file : startDir.listFiles()) {
            if (file.isDirectory()) {
                result.dirs.add(file);
                result.addAll(recurseDirs(file, regex));
            } else {
                if (file.getName().matches(regex)) {
                    result.files.add(file);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(walk("."));
        } else {
            for (String arg : args) {
                System.out.println(walk(arg));
            }
        }

    }
}
