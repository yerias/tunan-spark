package com.tunan.java.thread.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 分开处理异常数据和正常数据
 */
public class CompletableFutureExceptionally {

    private static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        try {
            exceptionally();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void exceptionally() throws ExecutionException, InterruptedException {

        // 创建异步执行任务
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(new Supplier<Double>() {
            @Override
            public Double get() {
                System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
                try {
                    TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(true){
                    throw new RuntimeException("Make RuntimeException");
                }
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        }, pool);

        // 捕获错误，抛出的异常作为入参传递给回调方法
        CompletableFuture<Double> cf2 = cf.exceptionally(new Function<Throwable, Double>() {
            @Override
            public Double apply(Throwable throwable) {
                System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
                try {
                    TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("error stack trace->");
                throwable.printStackTrace();
                System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
                return -1.1;
            }
        });

        // cf正常执行时执行的逻辑，如果执行异常则不调用此逻辑
        CompletableFuture<Void> cf3 = cf.thenAccept(new Consumer<Double>() {
            @Override
            public void accept(Double param) {
                System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
                try {
                    TimeUnit.MILLISECONDS.sleep(Constant.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("param->" + param);
                System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());
            }
        });

        System.out.println("main thread start,time->"+System.currentTimeMillis());
        // TODO
        //  cf2.get时，没有异常，但是依然有返回值，就是cf的返回值
        //  cf2没有指定，其result就是cf执行的结果，
        //  理论上cf2.get应该立即返回的，此处是等待了cf3执行完成后才返回
        System.out.println("run result->"+cf2.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());

    }
}
