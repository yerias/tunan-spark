package com.tunan.java.thread.job;


import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tunan
 *
 * 练习题1
 */
public class Ex1 {
    public static void main(String[] args) {

//        ExecutorService exec = Executors.newFixedThreadPool(5);
//        ExecutorService exec = Executors.newCachedThreadPool();
        ExecutorService exec = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 5; i++) {
            exec.submit(new Ex1RunnerA(),""+i);
        }
    }

    static class Ex1RunnerA implements Runnable{

        public Ex1RunnerA() {
            System.out.println(Thread.currentThread().getName()+"执行线程");
        }

        @Override
        public void run() {

            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName()+"消息: "+i);
                // 线程礼让，放弃当前线程，一起重新竞争资源
                Thread.yield();
            }

            System.out.println(Thread.currentThread().getName()+"该线程执行完成");
        }
    }
}
