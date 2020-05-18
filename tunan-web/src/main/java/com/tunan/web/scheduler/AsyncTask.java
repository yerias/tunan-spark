package com.tunan.web.scheduler;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class AsyncTask {


    public void task1() throws Exception{
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        System.out.println("task1 花费时间: "+(end-start));
    }

    public void task2() throws Exception{
        long start = System.currentTimeMillis();
        Thread.sleep(2000);
        long end = System.currentTimeMillis();
        System.out.println("task2 花费时间: "+(end-start));
    }

    public void task3() throws Exception{
        long start = System.currentTimeMillis();
        Thread.sleep(3000);
        long end = System.currentTimeMillis();
        System.out.println("task3 花费时间: "+(end-start));
    }
}
