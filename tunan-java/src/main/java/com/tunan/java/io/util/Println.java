package com.tunan.java.io.util;

import java.util.Arrays;
import java.util.Collection;

/**
 * 打印文件和目录的工具类
 */
public class Println {

    public static String pformat(Collection<?> c){
        if(c.size() == 0){
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (Object elem : c) {
            if(c.size() != 1){
                sb.append("\n ");
            }
            sb.append(elem);
        }

        if(c.size() != 1){
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String println(Collection<?> c){
        return pformat(c);
    }

    public static String println(Object[] o){
        return pformat(Arrays.asList(o));
    }

    public static void println(Collection<?> c,boolean print){
        System.out.println(pformat(c));
    }

    public static void println(Object[] o,boolean print){
        System.out.println(pformat(Arrays.asList(o)));
    }
}
