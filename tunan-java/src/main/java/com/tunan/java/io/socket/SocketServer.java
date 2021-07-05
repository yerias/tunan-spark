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

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private static class HandelMsg implements Runnable{
        Socket client;

        public HandelMsg(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;

            try {
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                writer = new PrintWriter(client.getOutputStream(),true);
                String line;

                long startTime = System.currentTimeMillis();
                while ((line = reader.readLine()) != null){
                    writer.println("服务端发送: "+line);
                    System.out.println("客户端发送的消息: "+line);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("花费时间: "+ (endTime - startTime)+"秒");


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(null != reader){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(null != writer){
                    writer.close();
                }

                if(null != client){
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
            serverSocket = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while(true){
                client = serverSocket.accept();
                System.out.println(client.getRemoteSocketAddress() + "地址客户端连接成功！");

                executorService.submit(new HandelMsg(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
