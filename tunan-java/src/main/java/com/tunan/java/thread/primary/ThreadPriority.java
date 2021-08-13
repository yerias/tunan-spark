package com.tunan.java.thread.primary;

/**
 * 线程的优先级
 * 线程将根据其优先级的大小来决定哪个线程会先运行，
 * 但是需要注意并非优先级越高就一定会先执行，哪个线程先执行将由 CPU 的调度决定。
 */
public class ThreadPriority {

    static class MyThread implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+ "运行，i = " + i) ;  // 取得当前线程的名字
            }
        }
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();

        Thread t1 = new Thread(myThread, "线程A");
        Thread t2 = new Thread(myThread, "线程B");
        Thread t3 = new Thread(myThread, "线程C");

        t1.setPriority(Thread.MIN_PRIORITY); // 优先级最低
        t2.setPriority(Thread.MAX_PRIORITY); // 优先级最高
        t3.setPriority(Thread.NORM_PRIORITY);  // 优先级最中等

        t1.start();
        t2.start();
        t3.start();
    }
}
