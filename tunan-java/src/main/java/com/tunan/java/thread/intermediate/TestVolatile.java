package com.tunan.java.thread.intermediate;

public class TestVolatile {

    public static void main(String[] args) {
        ThreadVolatile threadVolatile = new ThreadVolatile();
        new Thread(threadVolatile).start();
        threadVolatile.flag = false;
        System.out.println("已将flag置为" + threadVolatile.flag);

    }

    static class ThreadVolatile implements Runnable{

        // 主线程对参数进行修改，其他线程不一定马上可见
        // 主线程修改了flag等于false，但是其他线程拿的还是true
        boolean flag = true;

        @Override
        public void run() {
            System.out.println("Flag=" + flag);
        }
    }
}
