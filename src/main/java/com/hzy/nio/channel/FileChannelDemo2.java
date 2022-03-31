package com.hzy.nio.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo2 {

    public static void main(String[] args)throws IOException {
        //创建RandomAccessFile拿到FileChannel
        RandomAccessFile file = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\nio.txt","rw");
        FileChannel channel = file.getChannel();

        //创建Buffer
        ByteBuffer buf = ByteBuffer.allocate(1024);

        String str = "第一次写入";
        buf.clear();

        //写入内容
        buf.put(str.getBytes());
        buf.flip();

        //FileChannel完成写入
        //使用while循环是为了保证把全部数据都写入，因为单纯调用write不一定能把全部内容写入文件
        while(buf.hasRemaining()){
            //还有可以写的数据的时候
            channel.write(buf);
        }

        //关闭channel
        channel.close();
        file.close();
    }
}
