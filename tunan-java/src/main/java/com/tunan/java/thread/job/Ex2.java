package com.tunan.java.thread.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ex2 {

    static class Ex2Fibonacci implements Runnable{

        private int n = 0;

        public Ex2Fibonacci() {
        }

        public Ex2Fibonacci(int n) {
            this.n = n;
        }

        private int fib(int x){
            if(x < 2) {
                return 1;
            }
            return fib(x - 2) + fib(x - 1);
        }

        @Override
        public void run() {
            for(int i = 0; i < n; i++) {
                System.out.print(fib(i) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
//        ExecutorService exec = Executors.newFixedThreadPool(5);
//        ExecutorService exec = Executors.newCachedThreadPool();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            exec.submit(new Ex2Fibonacci(15));
        }
    }
}
