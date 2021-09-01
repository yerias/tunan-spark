package com.tunan.java.thread.primary2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Tunan
 *
 * sleep 线程睡眠，但是不会放弃锁
 */
public class SleepingTask extends LiftOff{

    @Override
    public void run() {

        try {
            while(countDown-- > 0){
                System.out.println(status());
                // 每个线程在1s内执行一次
                TimeUnit.MILLISECONDS.sleep(1000);
            }

        } catch (InterruptedException e){
            System.out.println("InterruptedException");
        }
    }


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new SleepingTask());
        }
        executorService.shutdown();
    }
}
