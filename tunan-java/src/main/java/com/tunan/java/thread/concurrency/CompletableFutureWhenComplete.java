package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 同时捕获正常数据和异常数据，不能返回结果值
 */
public class CompletableFutureWhenComplete {

    private static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        try {
            whenComplete();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void whenComplete() throws ExecutionException, InterruptedException {

        // 创建异步执行任务
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (true) {
                throw new RuntimeException("手动运行时异常");
            }
            System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
            return 1.2;
        });

        // cf执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，
        // 如果是正常执行的则传入的异常为null
        CompletableFuture<Double> cf2 = cf1.whenCompleteAsync((a, b) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (b != null) {
                System.out.println("error stack trace->");
                b.printStackTrace();
            } else {
                System.out.println("run succ,result->" + a);
            }
            System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());

        }, pool);

        System.out.println("main thread start wait,time->"+System.currentTimeMillis());
        // TODO 等待子任务执行完成
        //  如果cf是正常执行的，cf2.get的结果就是cf执行的结果
        //  如果cf是执行异常，则cf2.get会抛出异常
        System.out.println("run result->"+cf2.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
}
