package com.tunan.java.thread.intermediate;

/**
 * 可继承的本地线程的工具类
 */
public class InheritableThreadLocalUtils {

    private static final ThreadLocal<Integer> local = new InheritableThreadLocal<>();

    public static void set(Integer id){
        local.set(id);
    }

    public static Integer get(){
        return local.get();
    }

    public static void remove(){
        local.remove();
    }

}
