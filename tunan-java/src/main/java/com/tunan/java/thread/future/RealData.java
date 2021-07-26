package com.tunan.java.thread.future;


/**
 * IData实现，主要是模拟一个耗时的远程方法
 */
public class RealData implements IData{

    private String result;

    // 在构造器中实现
    public RealData(String str) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        // 假设一个相当耗时的远程方法
        for (int i = 0; i < 20; i++) {
            sb.append("i").append(i);
            Thread.sleep(1000);
        }

        System.out.println("构造器初始化完成");
        result = sb.append("_").append(str).toString();

    }

    // 构造器初始化完成，数据准备好了，获取数据
    @Override
    public String getResult() {
        System.out.println("构造器初始化完成，数据准备好了，获取数据");
        return result;
    }
}
