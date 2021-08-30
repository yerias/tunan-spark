package com.tunan.java.thread.concurrency;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListenableFutureTaskExample {

    public static void main(String[] args) {
        exeListenFuture();
    }

    public static void exeListenFuture() {
        System.out.println("开始执行");

        // 线程池，需要MoreExecutors.listeningDecorator()装饰，
        // 返回值由ExecutorService转换为ListeningExecutorService
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        // 两个任务
        Task t1 = new Task();
        Task t2 = new Task();

        t1.args = "task1";
        t2.args = "task2";

        // 提交两个任务，获取两个future
        ListenableFuture<String> future1 = executorService.submit(t1);
        ListenableFuture<String> future2 = executorService.submit(t2);

        // future.addListener()  不能带有返回值
        future2.addListener(() -> System.out.println("addListener不能带返回值"), executorService);

        // FutureCallBack接口可以对每个任务的成功或失败单独做出响应，带有返回值
        FutureCallback<String> futureCallback = new FutureCallback<String>() {

            @Override
            public void onSuccess(String s) {
                System.out.println("Futures.addCallback 能带返回值：" + s);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("出错,业务回滚或补偿");
            }
        };

        // 为任务绑定回调接口，任务完成自动返回结果，不会阻塞主进程
        Futures.addCallback(future1,futureCallback,executorService);

//        try {
//            System.out.println(future1.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        System.out.println("主线程end");
    }


    static class Task implements Callable<String> {
        String args;

        @Override
        public String call() throws Exception {
            TimeUnit.MILLISECONDS.sleep(2000);
            System.out.println("任务: " + args);
            return "done";
        }
    }
}