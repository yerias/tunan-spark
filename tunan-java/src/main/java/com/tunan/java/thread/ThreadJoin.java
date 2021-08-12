package com.tunan.java.thread;

/**
 * join ： 当前线程强制执行完成
 */

public class ThreadJoin {

    static class JoinThread implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName()+" 运行, i= "+i);
            }
        }
    }
    public static void main(String[] args) {

        JoinThread joinThread = new JoinThread();

        Thread thread = new Thread(joinThread, "子线程");

        thread.start();

        for (int i = 0; i < 50; i++) {
            if(i > 10){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("主线程正在运行 -> " +i);
        }
    }
}
