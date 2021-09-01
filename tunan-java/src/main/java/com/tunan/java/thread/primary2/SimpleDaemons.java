package com.tunan.java.thread.primary2;

import java.util.concurrent.TimeUnit;

/**
 * @author Tunan
 * 设置后台线程
 */
public class SimpleDaemons implements Runnable{

    @Override
    public void run() {
        try {
            while (true){
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(Thread.currentThread() + " " +this);
            }
        } catch (InterruptedException e) {
            System.out.println("sleep() interrupted");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread t1 = new Thread(new SimpleDaemons());
            t1.setDaemon(true);
            t1.start();
        }
        TimeUnit.MILLISECONDS.sleep(1500);
    }
}