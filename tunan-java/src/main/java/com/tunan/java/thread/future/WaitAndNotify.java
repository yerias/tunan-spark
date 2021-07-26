package com.tunan.java.thread.future;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 等待通知机制
 */
public class WaitAndNotify {

    private static Object object = new Object();
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {

        new Thread(new WaitThread(),"wait").start();

        TimeUnit.SECONDS.sleep(5);

        new Thread(new notifyThread(),"notify").start();
    }

    static class WaitThread implements  Runnable{

        @Override
        public void run() {
            // 获取对象的锁。
            synchronized (object){
                // 条件不满足，调用对象wait()方法。
                while (flag){
                    try {
                        System.out.println(Thread.currentThread().getName() +
                                "标记是true,开始调用线程的wait @" +
                                new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        // 等待另外线程通知，如果满足条件，继续余下操作执行。
                        object.wait();
                    } catch (Exception ex){
                        ex.printStackTrace();

                    }
                }
                System.out.println(Thread.currentThread().getName() + "wait执行完 @"+
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));

            }
        }
    }


    static class notifyThread implements Runnable{

        @Override
        public void run() {
            // 获取对象的锁。
            synchronized (object){
                try {
                    System.out.println(Thread.currentThread().getName() + "改成标记状态，调用notify" +
                            new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    // 修改条件。
                    flag = false;
                    // 调用对象的notify()或者notifyAll()方法通知等待的线程。
                    object.notify();
                    // 释放锁.
                    Thread.sleep(5000);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            synchronized (object){
                System.out.println(Thread.currentThread().getName() + "再次睡眠5s @" +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
