package com.tunan.java.thread.primary;

/**
 * 当一个线程运行时，另外一个线程可以直接通过interrupt()方法中断其运行状态。
 */
public class ThreadInterrupt {

    public static class MyThread implements Runnable{

        @Override
        public void run() {
            System.out.println("1、进入run()方法") ;

            try {
                // 休眠10s
                Thread.sleep(10000);
                System.out.println("2、已经完成了休眠") ;
            } catch (InterruptedException e) {
                System.out.println("3、休眠被终止") ;
                return ; // 返回调用处
            }
            System.out.println("4、run()方法正常结束") ;
        }
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        Thread thread = new Thread(myThread, "子线程");
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("3、休眠被终止") ;
        }
        thread.interrupt();
    }
}
