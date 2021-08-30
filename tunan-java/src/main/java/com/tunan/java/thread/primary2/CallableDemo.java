package com.tunan.java.thread.primary2;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @author Tunan
 * 从任务中返回值
 */
public class CallableDemo{

    static class TaskWithResult implements Callable<String> {

        private int id;

        public TaskWithResult() {
        }

        public TaskWithResult(int id) {
            this.id = id;
        }

        @Override
        public String call() throws Exception {
            return "result of TaskWithResult "+id;
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<String>> futures = new ArrayList<>();

        // 一次提交10个可以返回值的线程调用
        for (int i = 0; i < 10; i++) {
            Future<String> future = executorService.submit(new TaskWithResult(i));
            futures.add(future);
        }

        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
    }
}
