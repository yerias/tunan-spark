package com.tunan.java.thread.primary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 等待通知机制
 */
public class WaitAndNotify {

    // 初始化一个对象
    private static Object object = new Object();
    // 标识，如果要的资源准备好了，则修改该状态
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {

        // 线程A
        new Thread(new WaitThread(),"wait").start();
        TimeUnit.SECONDS.sleep(2);
        // 线程B在线程A后发生
        new Thread(new notifyThread(),"notify").start();
    }

    static class WaitThread implements  Runnable{

        @Override
        public void run() {
            // 获取同一个对象的锁。
            synchronized (object){
                // 资源没有准备好，没有修改标识位的状态，调用对象wait()方法。
                while (flag){
                    try {
                        System.out.println(Thread.currentThread().getName() +
                                "标记是true,开始调用线程的wait @" +
                                new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        // 释放锁，等待另外线程通知
                        object.wait();
                    } catch (Exception ex){
                        ex.printStackTrace();

                    }
                }
                // 打印执行完成
                System.out.println(Thread.currentThread().getName() + "wait执行完 @"+
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));

            }
        }
    }


    static class notifyThread implements Runnable{

        @Override
        public void run() {
            // 获取同一个对象的锁。上面的对象释放锁，这里获取锁
            synchronized (object){
                try {
                    System.out.println(Thread.currentThread().getName() + "改成标记状态，调用notify" +
                            new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    // 资源准备完成，修改标识位。
                    flag = false;
                    // 通知等待的线程可以就绪了。
                    object.notify();
                    // 睡眠5s，不释放锁
                    Thread.sleep(1000);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            // 再次争抢锁对象
            synchronized (object){
                System.out.println(Thread.currentThread().getName() + "再次睡眠1s @" +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                try {
                    // 睡1s，不释放锁
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
