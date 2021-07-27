package com.tunan.java.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ChannelSocketClient{

    // 定义多路复用器(考点)
    private Selector selector;
    // 端口
    private final static int PORT = 9000;
    // ByteBuffer的容量
    private final static int BUFF_SIZE = 2*1024;
    // 分配内存，创建ByteBuffer
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(BUFF_SIZE);

    private void initClient() throws IOException {
        // 创建多路复用器，不同的平台实现不同
        selector = Selector.open();
        // 创建SocketChannel,用来操作与Client的操作
        SocketChannel clientChannel = SocketChannel.open();
        // 设置非阻塞
        clientChannel.configureBlocking(false);
        // 连接到服务器
        clientChannel.connect(new InetSocketAddress(PORT));
        // clientChannel注册到多路复用器
        clientChannel.register(selector, SelectionKey.OP_CONNECT);

        while(true){
            // 阻塞，等待客户端发起操作
            System.out.println("客户端阻塞中");
            selector.select();
            System.out.println("客户端接受到事件，向下执行");
            // 有客户端的操作进来，才会走到下一步
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                // 拿到复用器的key
                SelectionKey key = iterator.next();
                // 拿到Key后删除该SelectionKey
                iterator.remove();
                // SelectionKey是一个连接
                if(key.isConnectable()){
                    System.out.println("连接事件");
                    doConnect(key);
                    // SelectionKey 是一个可读
                }else if(key.isReadable()){
                    System.out.println("可读事件");
                    doRead(key);
                }
            }
        }
    }

    private void doConnect(SelectionKey key) throws IOException {
        // 通过Key拿到Channel
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // 判断连接完成
        if(clientChannel.isConnectionPending()){
            clientChannel.finishConnect();
        }
        // 设置从Key里拿到的clientChannel为非阻塞
        clientChannel.configureBlocking(false);
        // 创建一个消息
        String info = "连接到服务器";
        byteBuffer.clear();
        byteBuffer.put(info.getBytes());
        // 声明可写
        byteBuffer.flip();
        // 写出数据
        clientChannel.write(byteBuffer);
        // 把连接上的clientChannel注册到多路复用器
        clientChannel.register(selector, SelectionKey.OP_READ);
//        clientChannel.close();
    }

    private void doRead(SelectionKey key) throws IOException {
        // 从接受到的SelectionKey中拿到ClientChannel
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // 从clientChannel中读取数据
        clientChannel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        String msg = new String(data).trim();
        System.out.println("服务端发送消息: "+msg);
        // 读取完成
        key.interestOps(SelectionKey.OP_READ);
        clientChannel.close();
    }

    public static void main(String[] args) {
        ChannelSocketClient client = new ChannelSocketClient();
        try {
            client.initClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
