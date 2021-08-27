package com.tunan.java.thread.concurrency;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListenableFutureTaskExample {

    public static void main(String[] args) {

        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    }

    public static void exeListenFuture() {
        System.out.println("开始执行");

        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        Task t1 = new Task();
        Task t2 = new Task();

        t1.args = "task1";
        t2.args = "task2";

        ListenableFuture<String> future1 = executorService.submit(t1);
        ListenableFuture<String> future2 = executorService.submit(t2);

        future2.addListener(() -> System.out.println("addListener不能带返回值"), executorService);


        /*
         * FutureCallBack接口可以对每个任务的成功或失败单独做出响应
         */



    }


    static class Task implements Callable<String> {
        String args;

        @Override
        public String call() throws Exception {
            TimeUnit.MILLISECONDS.sleep(2000);
            System.out.println("任务: " + args);
            return "dong";
        }
    }
}
