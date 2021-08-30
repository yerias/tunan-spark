package com.tunan.java.thread.primary2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Tunan
 */
public class DaemonFromFactory implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(Thread.currentThread()+": "+this);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for (int i = 0; i < 5; i++) {
            exec.execute(new DaemonFromFactory());
        }
        System.out.println("all daemons started");
        TimeUnit.MILLISECONDS.sleep(1500);
    }
}
