package com.tunan.java.thread.future;

public class Server {

    // 主线程调用
    public FutureData getData(String str) {
        // 主线程 new 回调函数
        final FutureData futureData = new FutureData();
        // 主线程创建一个子线程执行耗时的任务，并启动，让任务去执行，这里不管结果是什么
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("异步执行");
                try {
                    // 耗时的任务
                    RealData realData = new RealData(str);
                    // 耗时的任务的任务执行完了走这里
                    futureData.setResult(realData);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        // 主线程直接返回
        System.out.println("主线程直接返回");
        return futureData;
    }
}