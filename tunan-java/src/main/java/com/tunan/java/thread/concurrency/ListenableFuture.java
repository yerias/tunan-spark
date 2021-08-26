package com.tunan.java.thread.concurrency;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class ListenableFuture {

    public static void main(String[] args) {

        ListeningExecutorService executorService  = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("call execute ..");
                return new Random().nextInt(10);
            }
        });



    }
}
