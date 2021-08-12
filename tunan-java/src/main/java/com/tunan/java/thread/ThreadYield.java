package com.tunan.java.thread;

/**
 *  yield() 方法将一个线程的操作暂时让给其他线程执行
 */
public class ThreadYield {

    static class MyThread implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()
                        + "运行，i = " + i) ;  // 取得当前线程的名字

                if(i == 2){
                    System.out.println("线程礼让");
                    Thread.yield() ;    // 线程礼让
                }
            }
        }

        public static void main(String[] args) {
            MyThread myThread = new MyThread();
            Thread t1 = new Thread(myThread, "线程A");
            Thread t2 = new Thread(myThread, "线程B");

            t1.start();
            t2.start();
        }
    }
}
