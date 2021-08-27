package com.tunan.java.thread.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 包子和凉菜
 */
public class BumAndColdDishFuture {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        // 凉菜出餐
        Callable<String> cal = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.MILLISECONDS.sleep(1000);
                return "凉菜准备完毕";
            }
        };

        FutureTask<String> futureTask = new FutureTask<>(cal);
        new Thread(futureTask).start();

        // 等待蒸包子
        Callable<String> cal2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.MILLISECONDS.sleep(3000);
                return "包子准备完毕";
            }
        };

        FutureTask<String> futureTask1 = new FutureTask<>(cal2);
        new Thread(futureTask1).start();


        // get方法会阻塞，一直等到futureTask1.get()执行完成才返回
        // 凉菜比包子快2s，却要等包子好了一起上菜
        System.out.println(futureTask1.get());
        System.out.println(futureTask.get());

        long end = System.currentTimeMillis();
        System.out.println("准备完毕时间："+(end-start));
    }
}
