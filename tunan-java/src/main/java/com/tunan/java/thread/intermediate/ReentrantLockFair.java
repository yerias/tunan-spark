package com.tunan.java.thread.intermediate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockFair {

    static ReentrantLock lock = new ReentrantLock(true);

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            new Thread(new FairLock(i)).start();
        }
    }

    static class FairLock implements Runnable{

        Integer id;

        public FairLock(Integer id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // 线程之间间隔10ms
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 2; i++) {
                lock.lock();
                System.out.println(Thread.currentThread().getId()+"获得锁的线程："+id);
                lock.unlock();
            }
        }
    }
}
