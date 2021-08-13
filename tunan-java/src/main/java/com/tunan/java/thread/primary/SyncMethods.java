package com.tunan.java.thread.primary;

/**
 * 同步方法，锁住共享资源
 */
public class SyncMethods {

    private static class MyThread implements Runnable{

        private int ticket = 5;

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                this.sale();
            }
        }


        public synchronized void sale(){
            if(ticket > 0){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"卖票：ticket = " + ticket-- );
            }
        }
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();

        Thread t1 = new Thread(myThread, "线程A");
        Thread t2 = new Thread(myThread, "线程B");
        Thread t3 = new Thread(myThread, "线程C");

        t1.start();
        t2.start();
        t3.start();
    }
}
