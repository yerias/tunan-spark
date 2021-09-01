package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * TODO 组合查询
 *  applyToEither会将已经执行完成的任务的执行结果作为方法入参，并有返回值；
 *  acceptEither同样将已经执行完成的任务的执行结果作为方法入参，但是没有返回值；
 *  runAfterEither没有方法入参，也没有返回值。
 *  注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。
 */
public class CompletableFutureEither {

    private static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        try {
            taskEitherChain();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void  taskEitherChain() throws ExecutionException, InterruptedException {
        // 创建异步执行任务
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
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME-500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return 3.2;
        });
        //cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给result1,且有返回值
        CompletableFuture<Double> result1 = cf1.applyToEither(cf2, (result) -> {
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            System.out.println("job3 param result->" + result);
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
            return result;
        });

        //cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给result2无返回值
        CompletableFuture<Void> result2 = cf1.acceptEither(cf2, (result) -> {
            System.out.println(Thread.currentThread() + " start job4,time->" + System.currentTimeMillis());
            System.out.println("job4 param result->" + result);
            try {
                TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME - 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " exit job4,time->" + System.currentTimeMillis());
        });

        //result1和result2都执行完成后，执行result3，无入参，无返回值
        CompletableFuture<Void> result3 = result1.runAfterEither(result2, () -> {
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
        // 为什么job2 -> job4/job1 -> job3  ,   job4 -> job5
        System.out.println("cf1 run result->"+cf1.get());
        System.out.println("main thread start cf5.get(),time->"+System.currentTimeMillis());
        System.out.println("result3 run result->"+result3.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }

}
