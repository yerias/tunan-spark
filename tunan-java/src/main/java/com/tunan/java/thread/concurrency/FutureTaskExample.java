package com.tunan.java.thread.concurrency;

import java.util.concurrent.*;

/**
 * 普通的Future，get会阻塞进程
 */
public class FutureTaskExample {

    static class MyCallable implements Callable<String> {

        private long waitTime;

        public MyCallable(long waitTime) {
            this.waitTime = waitTime;
        }

        @Override
        public String call() throws Exception {
            TimeUnit.MILLISECONDS.sleep(waitTime);
            return Thread.currentThread().getName();
        }
    }

    public static void main(String[] args) {

        MyCallable callable1 = new MyCallable(2000);
        MyCallable callable2 = new MyCallable(4000);

        // FutureTask 是 Future 的实现
        FutureTask<String> task1 = new FutureTask<>(callable1);
        FutureTask<String> task2 = new FutureTask<>(callable2);

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(task1);
        exec.submit(task2);

        while (true) {
            try {
                if (task1.isDone() && task2.isDone()) {
                    System.out.println("Done!");
                    exec.shutdown();
                    return;
                }

                if (!task1.isDone()) {

                    // TODO 阻塞主线程
                    System.out.println("task1 输出: " + task1.get());
                }

                System.out.println("等待task2完成");
                // 1s 重试一次
                String task2Result = task2.get(1000, TimeUnit.MILLISECONDS);

                if (null != task2Result) {
                    System.out.println("task2 输出: " + task2.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {

            }
        }
    }
}
