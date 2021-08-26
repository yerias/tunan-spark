package com.tunan.java.thread.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * 包子和凉菜
 */
public class BumAndColdDishRunnable {

    static class Bum implements Runnable{

        // 包子3s出餐
        @Override
        public void run() {
            try {
                System.out.println("准备包子...");
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class ColdDish implements Runnable{


        // 凉菜1s出餐
        @Override
        public void run() {
            try {
                System.out.println("准备凉菜...");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        ColdDish coldDish = new ColdDish();
        Bum bum = new Bum();

        Thread t1 = new Thread(coldDish);
        t1.start();
        // 主线程进入等待池并等待t1线程执行完毕后才会被唤醒
        t1.join();
        Thread t2 = new Thread(bum);
        t2.start();
        // 主线程进入等待池并等待t2线程执行完毕后才会被唤醒
        t2.join();

        long end = System.currentTimeMillis();
        System.out.println("准备完毕时间："+(end-start));
    }
}
