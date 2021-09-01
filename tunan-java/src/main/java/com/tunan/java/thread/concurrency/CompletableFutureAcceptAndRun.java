package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * TODO 异步回调
 * thenAccept 同 thenApply 接收上一个任务的返回值作为参数，但是无返回值；
 * thenRun 的方法没有入参，也买有返回值。
 */
public class CompletableFutureAcceptAndRun {

    private static final ForkJoinPool pool=new ForkJoinPool();


    public static void main(String[] args) {
        try {
            taskChain();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void  taskChain() throws ExecutionException, InterruptedException {
        // 创建Future有返回值
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        }, pool);

        // 有入参有返回值
        CompletableFuture<Void> cf2 = cf1.thenApply((result) -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return "job2:" + result;
            // 有入参无返回值
        }).thenAccept((result -> {
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(result);
            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
            // 无入参无返回值
        })).thenRun(() -> {
            System.out.println(Thread.currentThread() + " start job4,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thenRun do something");
            System.out.println(Thread.currentThread() + " exit job4,time->" + System.currentTimeMillis());
        });

        System.out.println("main thread start cf.get(),time->"+System.currentTimeMillis());
        // cf1 等待子任务执行完成
        System.out.println("run result->"+cf1.get());
        System.out.println("main thread start cf2.get(),time->"+System.currentTimeMillis());
        // cf2 等待最后一个thenRun执行完成
        System.out.println("run result->"+cf2.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
}
