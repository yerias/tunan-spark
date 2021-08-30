package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class CompletableFutureApply {

    public static void main(String[] args) {
        try {
            thenApply();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void thenApply() throws ExecutionException, InterruptedException {
        ForkJoinPool pool=new ForkJoinPool();

        CompletableFuture<Double> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        }, pool);


        // supplyAsync关联的异步任务的返回值作为方法入参，传入到thenApply的方法中
        // thenApply这里实际创建了一个新的CompletableFuture实例
        CompletableFuture<String> thenApply = supplyAsync.thenApply((result) -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return "result: " + result;
        });

        System.out.println("main thread start cf.get(),time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("run result->"+supplyAsync.get());
        System.out.println("main thread start cf2.get(),time->"+System.currentTimeMillis());
        System.out.println("run result2->"+thenApply.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
}
