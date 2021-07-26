package com.tunan.java.thread.future;


/**
 * future类可以实现，代码的异步调用，
 * 程序调用耗时的网络或者IO相关的方法的时候，
 * 首先获得一个Future的代理类，
 * 同时线程并不会被阻塞。继续执行之后的逻辑，
 * 直到真正要使用远程调用返回的结果的时候，
 * 才需要调用future的get()方法。
 * 这样可以提高代码的执行效率。
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {

        // 主要作用就是让主线程去启动一个子线程执行任务，主线程马上返回
        Server server = new Server();
        FutureData data = server.getData("嘿嘿嘿");

        System.out.println("a");

        System.out.println("b");

        Thread.sleep(30000);

        // 真正要使用远程调用返回的结果的时候去拿子线程准备好的数据
        System.out.println("result is " + data.getResult());
    }
}
