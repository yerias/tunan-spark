package com.tunan.java.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 获取 new io 的channel
 */
public class FileChannelWriteAndRead {

    private static int BSIZE = 1024;

    public static void main(String[] args) throws IOException {

        // 写数据
        java.nio.channels.FileChannel fc = new FileOutputStream("tunan-java/out/out.txt").getChannel();
        fc.write(ByteBuffer.wrap("hello world ".getBytes()));
        fc.close();

        // 随机读写数据
        fc = new RandomAccessFile("tunan-java/out/out.txt","rw").getChannel();
        fc.position(fc.size());
        fc.write(ByteBuffer.wrap("li yuan rui ".getBytes()));
        fc.close();

        // 读数据
        fc = new FileInputStream("tunan-java/out/out.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        // 刷新读
        buff.flip();

        while (buff.hasRemaining()){
            // 读取
            System.out.println((char) buff.get());
        }
    }
}
