package com.tunan.java.thread.primary;

/**
 * 后台线程,主线程执行完了会退出,子线程后台继续执行
 */
public class ThreadDaemon {

    static class MyThread implements Runnable{

        @Override
        public void run() {
            System.out.println("进子线程");
            while (true){
                try {
                    Thread.sleep(300);
                    System.out.println(Thread.currentThread().getName() + "在运行") ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {

        MyThread thread = new MyThread();

        Thread t = new Thread(thread, "子线程");
        t.setDaemon(true);
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
