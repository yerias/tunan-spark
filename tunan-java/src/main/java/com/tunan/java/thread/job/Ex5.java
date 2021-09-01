package com.tunan.java.thread.job;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Ex5  {

    static class Ex5Fibonacci implements Callable<Integer>{

        private int n = 0;

        public Ex5Fibonacci() {
        }

        public Ex5Fibonacci(int n) {
            this.n = n;
        }

        private int fib(int x){
            if(x < 2) {
                return 1;
            }
            return fib(x - 2) + fib(x - 1);
        }

        @Override
        public Integer call() throws Exception {
            int result = 0;
            for (int i = 0; i < n; i++) {
                result += fib(n);
            }

            return result;
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(10);

        ArrayList<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Future<Integer> f = exec.submit(new Ex5Fibonacci(i));
            futures.add(f);
        }

        for (Future<Integer> future : futures) {
            try {
                // get() 会阻塞
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                exec.shutdown();
            }
        }
    }
}