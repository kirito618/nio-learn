package com.hzy.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 使用transferTo()方法进行两个Channel之间的数据传输
 * */
public class FileChannelDemo4 {

    public static void main(String[] args) throws IOException {
        RandomAccessFile from = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\nio.txt","rw");
        FileChannel fromChannel = from.getChannel();

        RandomAccessFile to = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\nio2.txt","rw");
        FileChannel toChannel = to.getChannel();

        long position = 0;
        long size = fromChannel.size();
        fromChannel.transferTo(position,size,toChannel);

        System.out.println("over!");
    }
}
