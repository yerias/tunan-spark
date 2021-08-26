package com.tunan.java.thread.intermediate;

/**
 * 死锁 : 两个线程都在等待对方先完成，造成程序的停滞，一般程序的死锁都是在程序运行时出现的。
 */



public class SyncDeadlock implements Runnable{
    static class ZhangSan{
        public void say(){
            System.out.println("张三对李四说：\"你给我画，我就把书给你\"") ;
        }

        public void get(){
            System.out.println("张三得到画了。") ;
        }
    }

    static class LiSi{
        public void say(){
            System.out.println("李四对张三说：\"你给我书，我就把画给你\"") ;
        }

        public void get(){
            System.out.println("李四得到书了。") ;
        }
    }

    // static修饰，锁的是类
    final static ZhangSan zs = new ZhangSan();
    final static LiSi ls = new LiSi();
    private boolean flag = false;
    @Override
    public void run() {
        if(flag){
            synchronized (zs){
                zs.say();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (ls){
                    zs.get();
                }
            }
        }else{
            synchronized (ls){
                ls.say();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (zs){
                    ls.get();
                }
            }
        }
    }

    public static void main(String[] args) {
        SyncDeadlock d1 = new SyncDeadlock();   // 控制张三
        SyncDeadlock d2 = new SyncDeadlock();   // 控制李四

        d1.flag = true;
        d2.flag = false;

        Thread t1 = new Thread(d1, "线程A");
        Thread t2 = new Thread(d2, "线程B");

        // 互相获取对方的锁，死锁
        t1.start();
        t2.start();
    }
}