package com.tunan.java.thread.future;

// 实现了IData的RealData包装类,相当标准的等待通知机制的类。
public class FutureData implements IData{

    private RealData realData;
    private volatile boolean isReal = false;

    // 主线程进来拿数据，数据没准备好，等待
    @Override
    public synchronized String getResult() {

        while(!isReal){
            try {
                System.out.println("线程进来拿数据，数据没准备好，等待");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.getResult();
    }


    // 子线程数据准备好了，通知主线程可以拿了
    public synchronized void setResult(RealData realData){
        if(isReal){
            return;
        }

        this.realData = realData;
        isReal = true;
        System.out.println("子线程数据准备好了，通知主线程可以拿了");
        notifyAll();
    }
}
