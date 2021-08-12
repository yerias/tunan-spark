package com.tunan.java.thread;


/**
 * 使用Runnable实现
 */


public class RunnableDeme {

    static class MyRunnable implements Runnable {

        // 线程名称
        private String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(name + "运行, i=" + i);
            }
        }
    }
    public static void main(String[] args) {
        MyRunnable runnableA = new MyRunnable("线程A");
        MyRunnable runnableB = new MyRunnable("线程B");

        new Thread(runnableA).start();
        new Thread(runnableB).start();

    }
}
