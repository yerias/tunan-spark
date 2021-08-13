package com.tunan.java.thread.primary;

/**
 * 取得和设置线程的名称
 */

public class ThreadName {

    static class MyThreadName implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName()+" 运行, i= "+i);
            }
        }
    }

    public static void main(String[] args) {

        MyThreadName nameThread = new MyThreadName();

        new Thread(nameThread,"线程A ").start();
        new Thread(nameThread,"线程B ").start();
    }
}
