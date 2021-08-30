package com.tunan.java.thread.concurrency;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ListenableFutureTaskChain {

    private static final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
                    15,
                    60,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<Runnable>(),
                    new ThreadFactoryBuilder()
                            .setNameFormat("me")
                            .build()));

    public static void main(String[] args) {
        multiTaskTransformAsync();
    }

    public static void multiTaskTransformAsync() {

        // 第一个任务
        ListenableFuture<String> task1 = executorService.submit(() -> {
            System.out.println("第一个任务开始执行");
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "第一个任务执行完成 ";
        });

        // 第二个任务，里面还获取到了第一个任务的结果
        AsyncFunction<String, String> task2 = new AsyncFunction<String, String>() {
            @Override
            public ListenableFuture<String> apply(String s) throws Exception {
                return executorService.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        System.out.println("第二个任务开始执行");
                        try {
                            TimeUnit.MILLISECONDS.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return s + "第二个任务执行完成 ";
                    }
                });
            }
        };

        // 第一个任务执行完成后执行第二个任务
        ListenableFuture<String> transform = Futures.transform(task1, task2, executorService);

        Futures.addCallback(transform, new FutureCallback<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println("结果: " + s);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println(throwable.getMessage());

            }
        }, executorService);
    }
}
