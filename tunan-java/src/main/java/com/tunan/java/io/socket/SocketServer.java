package com.tunan.java.io.socket;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    // new 线程池(考点，线程池的怎么实现的)
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    // 处理数据的线程
    private static class HandelMsg implements Runnable {
        Socket client;

        public HandelMsg(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // 定义读流
            BufferedReader reader = null;
            // 定义写流
            PrintWriter writer = null;

            try {
                // 从Socket中读取客户端发送过来的数据
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                // 通过Socket写数据到客户端
                writer = new PrintWriter(client.getOutputStream(), true);
                String line;

                long startTime = System.currentTimeMillis();
                while ((line = reader.readLine()) != null) {
                    writer.println("服务端发送: " + line);
                    System.out.println("客户端发送的消息: " + line);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("花费时间: " + (endTime - startTime) + "秒");


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (null != writer) {
                    writer.close();
                }

                if (null != client) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket client;

        try {
            // 定义一个服务器的Socket
            serverSocket = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 死循环
            while (true) {
                // 阻塞，等待客户端连接
                client = serverSocket.accept();
                System.out.println(client.getRemoteSocketAddress() + "地址客户端连接成功！");
                // 接入客户端的连接，交给子线程去处理
                executorService.submit(new HandelMsg(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}