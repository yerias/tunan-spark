package com.tunan.java.thread.primary;

/**
 * 继承Thread实现，它也是实现了Runnable
 */


public class ThreadDemo {

    static class MyThread extends Thread{

        private String name;

        public MyThread(String name){
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(name + "运行, i=" + i);        }
        }
    }
    public static void main(String[] args) {
        MyThread myThreadA = new MyThread("线程A");
        MyThread myThreadB = new MyThread("线程B");

        myThreadA.start();
        myThreadB.start();
    }
}
