package com.tunan.java.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QQServer {

    private final Integer PORT = 9000;
    //消息分隔符
    private final String SEPARATOR = "[|]";
    //字符集
    private final Charset CHARSET = StandardCharsets.UTF_8;
    //缓存
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private ServerSocketChannel socketChannel;
    private Selector selector;

    //将用户对应的channel对应起来
    private Map<String, SocketChannel> onlineUsers = new HashMap<String, SocketChannel>();

    public void startServer() throws IOException {
        // NIO server初始化固定流程：5步
        selector = Selector.open();                         // 1.selector open
        socketChannel = ServerSocketChannel.open();         // 2.ServerSocketChannel open
        socketChannel.bind(new InetSocketAddress(PORT));    // 3.serverChannel绑定端口
        socketChannel.configureBlocking(false);             // 4.设置NIO为非阻塞模式
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);// 5.将channel注册在选择器上

        //NIO server处理数据固定流程:5步
        SocketChannel client;
        SelectionKey key;
        Iterator<SelectionKey> keys;

        while (true) {
            //1.用select()方法阻塞，一直到有可用连接加入
            selector.select();
            //2.到了这步，说明有可用连接到底，取出所有可用连接
            keys = selector.selectedKeys().iterator();
            //3.遍历
            while (keys.hasNext()) {
                key = keys.next();
                //4.对每个连接感兴趣的事做不同的处理
                if (key.isAcceptable()) {
                    //对于客户端连接，注册到服务端
                    client = socketChannel.accept();
                    //获取客户端首次连接
                    client.configureBlocking(false);
                    //不用注册写，只有当写入量大，或写需要争用时，才考虑注册写事件
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端：" + client.getRemoteAddress() + "，建立连接");
                    client.write(CHARSET.encode("请输入自定义用户名："));
                }

                if (key.isReadable()) {
                    //通过key取得客户端channel
                    client = (SocketChannel) key.channel();
                    StringBuilder sb = new StringBuilder();
                    //多次使用的缓存，用前要先清空
                    buffer.clear();
                    try {
                        while (client.read(buffer) > 0) {
                            //将写模式转换为读模式
                            buffer.flip();
                            sb.append(CHARSET.decode(buffer));
                        }
                    } catch (Exception ex) {
                        //如果client.read(buffer)抛出异常，说明此客户端主动断开连接，需做下面处理
                        //关闭channel
                        client.close();
                        //将channel对应的key置为不可用
                        key.cancel();
                        //将问题连接从map中删除
                        onlineUsers.values().remove(client);
                        System.out.println("用户'" + key.attachment().toString() + "'退出连接，当前用户列表：" + onlineUsers.keySet().toString());
                        //跳出循环
                        continue;
                    }
                    //处理消息体
                    if (sb.length() > 0) this.processMsg(sb.toString(), client, key);
                }
                //5.处理完一次事件后，要显式的移除
                keys.remove();
            }
        }

    }

    /**
     * 处理客户端传来的消息
     *
     * @param msg 格式：user_to|body|user_from
     * @throws IOException
     * @Key 这里主要用attach()方法，给通道定义一个表示符
     */
    private void processMsg(String msg, SocketChannel client, SelectionKey key) throws IOException {
        String[] ms = msg.split(SEPARATOR);
        if (ms.length == 1) {
            //输入的是自定义用户名
            String user = ms[0];
            if (onlineUsers.containsKey(user)) {
                client.write(CHARSET.encode("当前用户已存在，请重新输入用户名："));
            } else {
                onlineUsers.put(user, client);
                //给通道定义一个表示符
                /*
                    可以将一个对象或者更多的信息附着到SelectionKey上，这样就能方便的识别某个给定的通道。
                        selectionKey.attach('zhangsan');
                        String zhangsan = selectionKey.attachment();
                 */
                key.attach(user);
                // `|`字符来作为消息之间的分割符
                client.write(CHARSET.encode("您的昵称通过验证 " + user + "|"));
                String welCome = "\t欢迎'" + user + "'上线，当前在线人数" + onlineUsers.size() + "人。用户列表：" + onlineUsers.keySet().toString();
                this.broadCast(welCome + "|");     //给所用用户推送上线信息，包括自己
            }
        } else if (ms.length == 3) {
            String user_to = ms[0];
            String msg_body = ms[1];
            String user_from = ms[2];

            SocketChannel channel_to = onlineUsers.get(user_to);
            if (channel_to == null) {
                client.write(CHARSET.encode("用户'" + user_to + "'不存在，当前用户列表：" + onlineUsers.keySet().toString() + "\n"));
            } else {
                channel_to.write(CHARSET.encode("来自'" + user_from + "'的消息：" + msg_body + "\n"));
            }
        }
    }

    //map中的有效数量已被很好的控制，可以从map中获取，也可以用下面的方法取
    private int getOnLineNum() {
        int count = 0;
        Channel channel;
        for (SelectionKey k : selector.keys()) {
            channel = k.channel();
            if (channel instanceof SocketChannel) {    //排除ServerSocketChannel
                count++;
            }
        }
        return count;
    }

    //广播上线消息
    private void broadCast(String msg) throws IOException {
        Channel channel;
        // 拿到所有复用选择器
        for (SelectionKey k : selector.keys()) {
            // 拿到每个复用选择器的channel
            channel = k.channel();
            // 如果这个channel是一个客户端的channel
            if (channel instanceof SocketChannel) {
                // 转换成客户端channel (SocketChannel)
                SocketChannel client = (SocketChannel) channel;
                // 写出数据到SocketChannel
                client.write(CHARSET.encode(msg));
            }
        }
    }

    public static void main(String[] args) {
        try {
            new QQServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
