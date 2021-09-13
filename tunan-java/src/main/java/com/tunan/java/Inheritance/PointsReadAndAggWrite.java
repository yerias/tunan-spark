package com.tunan.java.Inheritance;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * 分散读取/聚合写入
 *
 * 使用场景就是可以使用一个缓冲区数组，自动地根据需要去分配缓冲区的大小。
 * 可以减少内存消耗。网络IO也可以使用，这里就不写例子演示了。
 */
public class PointsReadAndAggWrite {

    public static void main(String[] args) throws IOException {

        File inFile = new File("tunan-java/file/1.txt");
        File outFile = new File("tunan-java/file/2.txt");

        FileInputStream inputStream = new FileInputStream(inFile);
        FileChannel inputStreamChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream(outFile);
        FileChannel outputStreamChannel = outputStream.getChannel();

        ByteBuffer a = ByteBuffer.allocate(5);
        ByteBuffer b = ByteBuffer.allocate(5);
        ByteBuffer c = ByteBuffer.allocate(5);

        ByteBuffer[] buffers = {a, b, c};
        long read;
        long sumLength = 0;
        while ((read = inputStreamChannel.read(buffers)) != -1) {
            sumLength += read;
//            Arrays.stream(buffers)
//                    .map(buffer -> "position=" + buffer.position() + ",limit=" + buffer.limit())
//                    .forEach(System.out::println);

            for (ByteBuffer buffer : buffers) {
                System.out.println("position=" + buffer.position() + ",limit=" + buffer.limit());
            }


//            Arrays.stream(buffers).forEach(Buffer::flip);
            for (ByteBuffer buffer : buffers) {
                buffer.flip();
            }

            outputStreamChannel.write(buffers);
//            Arrays.stream(buffers).forEach(Buffer::clear);

            for (ByteBuffer buffer : buffers) {
                buffer.clear();
            }
        }
        System.out.println("总长度:" + sumLength);
        //关闭通道
        outputStream.close();
        inputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();
    }
}
