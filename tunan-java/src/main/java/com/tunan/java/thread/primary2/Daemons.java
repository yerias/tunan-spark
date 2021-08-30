package com.tunan.java.thread.primary2;

import java.util.concurrent.TimeUnit;

/**
 * @author Tunan
 */
public class Daemons {

    static class Daemon implements Runnable{

        private Thread[] t = new Thread[10];

        @Override
        public void run() {
            for (int i = 0; i < t.length; i++) {
                t[i] = new Thread(new DaemonSpawn());
                t[i].start();
                System.out.println("DaemonSpawn "+i+" started, ");
            }

            for (int i = 0; i < t.length; i++) {
                // 父线程是后台线程，这里的所有线程都是后台线程
                System.out.println("t["+i+"].isDaemon() = "+t[i].isDaemon()+".");
            }

            while (true){
                Thread.yield();
            }
        }
    }

    static class DaemonSpawn implements Runnable{

        @Override
        public void run() {
            while (true){
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 后台线程,它创建的任何子线程都会被自动设置为后台线程
        Thread t = new Thread(new Daemon());
        t.setDaemon(true);
        t.start();
        System.out.println("t.idDaemon() = "+t.isDaemon()+".");
        TimeUnit.MILLISECONDS.sleep(1000);
    }
}
