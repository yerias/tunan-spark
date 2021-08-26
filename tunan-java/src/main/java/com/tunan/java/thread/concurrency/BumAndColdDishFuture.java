package com.tunan.java.thread.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * 包子和凉菜
 */
public class BumAndColdDishFuture {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        // 凉菜出餐


        long end = System.currentTimeMillis();
        System.out.println("准备完毕时间："+(end-start));
    }
}
