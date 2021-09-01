package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * TODO 组合处理
 *  thenCombine会将两个任务的执行结果作为方法入参传递到指定方法中，且该方法有返回值；
 *  thenAcceptBoth同样将两个任务的执行结果作为方法入参，但是无返回值；
 *  runAfterBoth没有入参，也没有返回值。注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。
 */
public class CompletableFutureCombineAndBoth {

    private static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        try {
            taskCombineChain();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void  taskCombineChain() throws ExecutionException, InterruptedException {
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
            return 3.2;
        });

        //cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3,且有返回值
        CompletableFuture<Double> result1 = cf1.thenCombineAsync(cf2, (a, b) -> {

            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            System.out.println("job3 param a->" + a + ",b->" + b);
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
            return a + b;
        });

        //cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3,无返回值
        CompletableFuture<Void> result2 = cf1.thenAcceptBothAsync(cf2, (a, b) -> {

            System.out.println(Thread.currentThread() + " start job4,time->" + System.currentTimeMillis());
            System.out.println("job4 param a->" + a + ",b->" + b);
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME-500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " exit job4,time->" + System.currentTimeMillis());
        });

        //result1和result2都执行完成后，执行result3，无入参，无返回值
        CompletableFuture<Void> result3 = result2.runAfterBothAsync(result1, () -> {
            System.out.println(Thread.currentThread() + " start job5,time->" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME - 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("result3 do something");
            System.out.println(Thread.currentThread() + " exit job5,time->" + System.currentTimeMillis());
        });


        System.out.println("main thread start cf.get(),time->"+System.currentTimeMillis());
        // 等待子任务执行完成，只有cf1调用get()方法，但实际上所有的job都执行完了才返回
        // 使用的是主线程?
        //  - 异步执行更好
        System.out.println("cf1 run result->"+cf1.get());
        System.out.println("main thread start cf5.get(),time->"+System.currentTimeMillis());
//        System.out.println("result3 run result->"+result3.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
}
