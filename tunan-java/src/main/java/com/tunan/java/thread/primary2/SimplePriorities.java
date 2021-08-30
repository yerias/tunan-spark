package com.tunan.java.thread.primary2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tunan
 * 设置优先级
 */
public class SimplePriorities implements Runnable{

    private int countDown = 5;
    private volatile double d;
    private final int priorities;

    public SimplePriorities(int priorities) {
        this.priorities = priorities;
    }

    @Override
    public String toString() {
        return Thread.currentThread()+": "+countDown;
    }

    @Override
    public void run() {
        // 在run()里面设置当前线程的优先级
        Thread.currentThread().setPriority(priorities);
        while(true){
            for (int i = 0; i < 100000; i++) {
                d += (Math.PI + Math.E) / (double)i;
                if(i % 1000 == 0){
                    // 让步
                    Thread.yield();
                }
            }
            System.out.println(this);
            if(--countDown == 0){
                return;
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        exec.shutdown();
    }
}
