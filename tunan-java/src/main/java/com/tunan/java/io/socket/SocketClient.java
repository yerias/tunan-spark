package com.tunan.java.io.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) throws IOException {

        // 定义连接、读流、写流
        Socket client = null;
        PrintWriter writer = null;
        BufferedReader reader = null;

        try {
            // 初始化Socket
            client = new Socket();
            // 连接到服务器
            client.connect(new InetSocketAddress("localhost",9000));
            // 向服务器通过Socket发送数据
            writer = new PrintWriter(client.getOutputStream());
            writer.println("hello");
            writer.flush();

            // 接受服务器发送过来的数据
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("来自服务器的消息: "+reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != client){
                client.close();
            }

            if(null != writer){
                writer.close();
            }

            if(reader != null) {
                reader.close();
            }
        }
    }
}
