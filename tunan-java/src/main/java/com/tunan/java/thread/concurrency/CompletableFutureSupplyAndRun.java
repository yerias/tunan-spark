package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * TODO 创建异步任务
 *  创建CompletableFuture的两种方式
 */
public class CompletableFutureSupplyAndRun  {

    public static void main(String[] args) {

        try {
//            supplyAsync();
            runAsync();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // supplyAsync()有返回值
    public static void supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted");
            }
            System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
            return 1.2;
        });
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("run result->" + cf.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    // runAsync()无返回值
    public static void runAsync() throws ExecutionException, InterruptedException {
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted");
            }
            System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
        });
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("run result->" + cf.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }
}
