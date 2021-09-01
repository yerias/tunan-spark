package com.tunan.java.thread.primary2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tunan
 * 发射倒计时
 */
public class LiftOff implements Runnable{

    // 倒数10s
    protected int countDown = 10;

    private static int taskCount = 0;
    private final int id = taskCount++;

    public LiftOff() {
    }

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status(){
        return "#"+id+"("+(countDown>0?countDown:"LiftOff!")+"),";
    }

    @Override
    public void run() {
        while(countDown-- > 0){
            System.out.println(status());
            // 主动放弃，重新争抢锁资源
            Thread.yield();
        }

    }

    public static void main(String[] args) {
//        LiftOff liftOff = new LiftOff();
//        liftOff.run();
//
//        new Thread(liftOff).start();

//        for (int i = 0; i < 5; i++) {
//            new Thread(new LiftOff()).start();
//        }

//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < 5; i++) {
//            executorService.execute(new LiftOff());
//        }

//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        for (int i = 0; i < 5; i++) {
//            executorService.execute(new LiftOff());
//        }

//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        for (int i = 0; i < 5; i++) {
//            executorService.execute(new LiftOff());
//        }

//        executorService.shutdown();

        System.out.println("等待发射!");
    }
}
