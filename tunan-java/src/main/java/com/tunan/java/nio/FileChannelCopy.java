package com.tunan.java.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * channel 之间，复制数据
 */
public class FileChannelCopy {

    private static int BSIZE = 1024;

    public static void main(String[] args) throws IOException {

        String inFile = "tunan-java/data/word.txt";
        String outFile = "tunan-java/out/out.txt";

        FileChannel
                in = new FileInputStream(inFile).getChannel(),
                out = new FileOutputStream(outFile).getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(BSIZE);

        while (in.read(buffer) != -1){
            // flip 准备数据可以被write读取
            buffer.flip();
            out.write(buffer);
            // clear 清空指针，做好准备接收数据
            buffer.clear();
        }
    }
}
