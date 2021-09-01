package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * TODO 组合执行
 *  allOf等待所有任务执行完成才执行cf4，如果有一个任务异常终止，则cf4.get时会抛出异常，都是正常执行，cf4.get返回null
 *  anyOf是只有一个任务执行完成，无论是正常执行或者执行异常，都会执行cf4，cf4.get的结果就是已执行完成的任务的执行结果
 */
public class CompletableFutureAllOfAndAnyOf {

    private static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        try {
            taskAllOfAndAnyOf();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void taskAllOfAndAnyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        });

        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME - 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return 2.2;
        });

        CompletableFuture<Double> cf3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME - 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            throw new RuntimeException("test");
            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
            return 3.2;
        });

        // allof等待所有任务执行完成才执行result，如果有一个任务异常终止，则result.get时会抛出异常，都是正常执行，result.get返回null
        // anyOf是只有一个任务执行完成，无论是正常执行或者执行异常，都会执行result，result.get的结果就是已执行完成的任务的执行结果
        CompletableFuture result = CompletableFuture.allOf(cf1, cf2, cf3).whenComplete((a, b) -> {
            System.out.println("a: "+a+", b: "+b);
            if (b != null) {
                System.out.println("error stack trace->");
                b.printStackTrace();
            } else {
                System.out.println("run succ result->" + a);
            }
        });


        System.out.println("main thread start cf4.get(),time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("result run result->"+result.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
}
