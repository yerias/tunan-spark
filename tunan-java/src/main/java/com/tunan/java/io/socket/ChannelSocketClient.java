package com.tunan.java.io.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ChannelSocketClient{

    private Selector selector;
    private final static int PORT = 9000;
    private final static int BUFF_SIZE = 8 * 1024;
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(BUFF_SIZE);

    private void initClient() throws IOException {
        selector = Selector.open();
        SocketChannel clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);
        clientChannel.connect(new InetSocketAddress(PORT));
        clientChannel.register(selector, SelectionKey.OP_CONNECT);

        while(true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isConnectable()){
                    doConnect(key);
                }else if(key.isReadable()){
                    doRead(key);
                }
            }
        }
    }

    private void doConnect(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        if(clientChannel.isConnectionPending()){
            clientChannel.finishConnect();
        }

        clientChannel.configureBlocking(false);
        String info = "服务端你好！";
        byteBuffer.clear();
        byteBuffer.put(info.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        clientChannel.write(byteBuffer);
        clientChannel.register(selector, SelectionKey.OP_READ);
//        clientChannel.close();

    }

    private void doRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        clientChannel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        String msg = new String(data).trim();
        System.out.println("服务端发送消息: "+msg);
        key.interestOps(SelectionKey.OP_READ);
        clientChannel.close();
//        key.channel();
//        key.selector().close();


        //读取服务端的响应
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        int readBytes = clientChannel.read(buffer);
//        String content = "";
//        if (readBytes > 0) {
//            buffer.flip();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            content += new String(bytes);
////            stop = true;
//        } else if (readBytes < 0) {
//            //对端链路关闭
//            key.channel();
//            clientChannel.close();
//        }
//        System.out.println(content);


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
