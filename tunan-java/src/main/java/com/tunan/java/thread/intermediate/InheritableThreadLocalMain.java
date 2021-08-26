package com.tunan.java.thread.intermediate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程内部存储一个id，给子线程使用
 */
public class InheritableThreadLocalMain {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            executorService.execute(new TaskThread(i));
        }
    }

    static class TaskThread implements Runnable{

        Integer taskId;

        public TaskThread(Integer taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            InheritableThreadLocalUtils.set(taskId);
            System.out.println("taskId: " + taskId);

            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

            // 线程内部启用一个线程
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    System.out.println("childTaskId: "+InheritableThreadLocalUtils.get());
                }
            });
        }
    }
}