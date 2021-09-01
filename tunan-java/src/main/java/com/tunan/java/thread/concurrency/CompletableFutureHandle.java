package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * TODO 异步回调
 *  同时捕获正常数据和异常数据，有返回值
 *  跟whenComplete基本一致，区别在于handle的回调方法有返回值，
 *  且handle方法返回的CompletableFuture的result是回调方法的执行结果或者回调方法执行期间抛出的异常，
 *  与原始CompletableFuture的result无关了。
 */
public class CompletableFutureHandle {
    private static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        try {
            handle();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void handle() throws ExecutionException, InterruptedException {

        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (true) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        });

        CompletableFuture<String> cf2 = cf1.handle((a, b) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (b != null) {
                return "run error";
            } else {
                return "run success";
            }
        });
        System.out.println("main thread start wait,time->" + System.currentTimeMillis());
        // 等待子任务执行完成
        // get的结果是cf2的返回值，跟cf没关系了
        System.out.println("run result->" + cf2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }
}