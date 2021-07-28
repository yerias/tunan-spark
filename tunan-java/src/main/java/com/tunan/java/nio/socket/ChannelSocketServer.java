package com.tunan.java.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ChannelSocketServer {

    //创建一个选择器
    private Selector selector;
    private final static int PORT = 9000;
    private final static int BUFF_SIZE = 8 * 1024;

    private void initServer() throws IOException {
        //创建通道管理器对象selector
        this.selector = Selector.open();
        //创建一个通道对象channel
        ServerSocketChannel channel = ServerSocketChannel.open();
        //将通道设置为非阻塞
        channel.configureBlocking(false);
        //将通道绑定端口
        channel.socket().bind(new InetSocketAddress(PORT));
        // 将上述的通道管理器和通道绑定，并为该通道注册OP_ACCEPT事件
        // 注册事件后，当该事件到达时，selector.select()会返回（一个key），如果该事件没到达selector.select()会一直阻塞
        // ServerSocketChannel只有OP_ACCEPT可用，OP_CONNECT,OP_READ,OP_WRITE用于SocketChannel
        channel.register(selector, SelectionKey.OP_ACCEPT);

        //轮询
        while (true) {
            //这是一个阻塞方法，一直等待直到有数据可读，返回值是key的数量（可以有多个）
            System.out.println("阻塞中,等待事件");
            selector.select();
            System.out.println("监听到事件,向下执行");
            //如果channel有数据了，将生成的key访入keys集合中
            Set<SelectionKey> keys = selector.selectedKeys();
            //得到这个keys集合的迭代器
            Iterator<SelectionKey> iterator = keys.iterator();
            //使用迭代器遍历集合
            while (iterator.hasNext()) {
                //得到集合中的一个key实例
                SelectionKey key = iterator.next();
                //拿到当前key实例之后记得在迭代器中将这个元素删除，非常重要，否则会出错
                iterator.remove();
                //判断当前key所代表的channel是否在Acceptable状态，如果是就进行接收
                if (key.isAcceptable()) {
                    System.out.println("接受连接");
                    doAccept(key);
                } else if (key.isReadable()) {
                    System.out.println("可读取");
                    doRead(key);
                } else if (key.isWritable()) {
                    System.out.println("可写出");
                    doWrite(key);
                }else if (key.isConnectable()) {
                    System.out.println("连接成功！");
                }
            }
        }
    }

    /**
     * 接收连接
     *
     * @param key
     * @throws IOException
     */
    private void doAccept(SelectionKey key) throws IOException {
        // 拿到了Server端的Channel
        ServerSocketChannel serverChanel = (ServerSocketChannel) key.channel();
        // 获取客户端连接
        SocketChannel clientChannel = serverChanel.accept();
        // 切换到非阻塞模式
        clientChannel.configureBlocking(false);
        // 将该通道注册到选择器上
        clientChannel.register(key.selector(), SelectionKey.OP_READ);
        //将key对应Channel设置为准备接受其他请求
        key.interestOps(SelectionKey.OP_ACCEPT);
    }

    private void doRead(SelectionKey key) throws IOException {
        // 获取当前选择器上 "读就绪" 状态的通道
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // 分配内存
        ByteBuffer buffer = ByteBuffer.allocate(BUFF_SIZE);
        // 读取数据
        try {
            int byteRead = clientChannel.read(buffer);
            if (byteRead > 0){
                buffer.flip();
                // 判断消息是否发送完成
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                String info = new String(data);
                System.out.println("客户端发送过来的消息: "+info);
            }
            doWrite(clientChannel);
            // 这里只能是OP_READ
            key.interestOps(SelectionKey.OP_READ);
        } catch (Exception e) {
            key.cancel();
            if (clientChannel != null) {
                key.channel().close();
            }
        }
    }

    //
    private void doWrite(SelectionKey key) throws IOException {
        byte[] info = "服务器已连接！".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(info.length);
        byteBuffer.put(info);
        byteBuffer.flip();
        SocketChannel clientChannel = (SocketChannel) key.channel();
        clientChannel.write(byteBuffer);
        if (!byteBuffer.hasRemaining()) {
            System.out.println("服务器: 发送成功");
        }
        byteBuffer.clear();
        key.interestOps(SelectionKey.OP_READ);
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req = "服务器已连接".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);
        if (!byteBuffer.hasRemaining()) {
            System.out.println("服务器: 发送成功");
        }
    }

    public static void main(String[] args) {
        ChannelSocketServer server = new ChannelSocketServer();
        try {
            server.initServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
