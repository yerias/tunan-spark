package com.tunan.java.thread;


/**
 * sleep : 线程休眠
 */
public class ThreadSleep {

    static public class MyThread implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" 运行, i= "+i);
            }
        }
    }

    public static void main(String[] args) {

        MyThread threadSleep = new MyThread();

        new Thread(threadSleep,"子线程").start();

    }
}
