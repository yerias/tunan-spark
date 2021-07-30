package com.tunan.java.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class QQClient {

    private final Integer PORT = 9000;
    private final String SEPARATOR = "|";
    //字符集
    private final Charset CHARSET = StandardCharsets.UTF_8;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private SocketChannel socketChannel;
    private Selector selector;
    private String name = "";
    //服务端断开，客户端的读事件不会一直发生（与服务端不一样）
    private boolean flag = true;


    Scanner scanner = new Scanner(System.in);

    public void startClient() throws IOException {
        //客户端初始化固定流程：4步
        //1.打开Selector
        selector = Selector.open();
        //2.连接服务端，这里默认本机的IP
        socketChannel = SocketChannel.open(new InetSocketAddress(PORT));
        //3.配置此channel非阻塞
        socketChannel.configureBlocking(false);
        //4.将channel的读事件注册到选择器
        socketChannel.register(selector, SelectionKey.OP_READ);

        /*
         * 因为等待用户输入会导致主线程阻塞
         * 所以用主线程处理输入，新开一个线程处理读数据
         */
        //开一个异步线程处理读
        new Thread(new ClientReadThread()).start();

        String line = "";
        while (flag) {
            line = scanner.nextLine();
            if (line.equalsIgnoreCase("")) {
                System.out.println("不允许输入空字符串");
                continue;
            } else if ("".equals(name) && line.split("[|]").length == 1) {
                // 啥也不干
                // 如果姓名已经初始化过了，且长度为2.说明这是正常的发送格式
            } else if (!"".equals(name) && line.split("[|]").length == 2) {
                // 这里的name，是注册的name
                line = line + SEPARATOR + name;
            } else {
                System.out.println("输入不合法，请重新输入：");
                continue;
            }
            try {
                //控制台输入的数据，发送到服务端
                socketChannel.write(CHARSET.encode(line));
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "客户端主线程退出连接！！");
            }
        }
    }


    private class ClientReadThread implements Runnable {
        @Override
        public void run() {
            Iterator<SelectionKey> keys;
            SelectionKey key;
            SocketChannel client;

            try {
                while (flag) {
                    //调用此方法一直阻塞，直到有channel可用
                    selector.select();
                    keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        key = keys.next();
                        // TODO 处理读事件
                        if (key.isReadable()) {
                            // 拿得传输数据的channel
                            client = (SocketChannel) key.channel();
                            buffer.clear();
                            StringBuilder msg = new StringBuilder();
                            try {
                                while (client.read(buffer) > 0) {
                                    //将写模式转换为读模式
                                    buffer.flip();
                                    msg.append(CHARSET.decode(buffer));
                                }

                            } catch (Exception ex) {
                                System.out.println(ex.getMessage() + ",客户端'" + key.attachment().toString() + "'读线程退出！！");
                                stopMainThread();
                            }
                            // 使用 | 切分消息，处理沾包
                            String[] splits = msg.toString().split("[|]");
                            // 拿到每一条消息
                            for (String message : splits) {
                                // 如果返回的消息是空就跳出
                                if (message.equalsIgnoreCase("")) {
                                    continue;
                                }
                                // 如果用户是第一次注册，拿到用户名
                                if (message.contains("您的昵称通过验证")) {
                                    String[] nameValid = message.split(" ");
                                    name = nameValid[1];
                                    key.attach(name);
                                }
                                System.out.println(message);
                            }
                        }
                        keys.remove();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void stopMainThread() {
        flag = false;
    }

    public static void main(String[] args) {
        try {
            new QQClient().startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
