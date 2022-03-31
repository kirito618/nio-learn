package com.hzy.nio.channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通道之间数据的传输
 * */
public class FileChannelDemo3 {

    //使用transferFrom()方法实现两通道互传
    public static void main(String[] args) throws IOException {
        //创建两个FileChannel
        RandomAccessFile from = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\nio.txt","rw");
        FileChannel fromChannel = from.getChannel();

        RandomAccessFile to = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\nio2.txt","rw");
        FileChannel toChannel = to.getChannel();

        //把from的数据写入到to里面去,从position位置开始传输
        long position = 0;
        long size = fromChannel.size();
        toChannel.transferFrom(fromChannel,position,size);

        System.out.println("over!");
        fromChannel.close();
        toChannel.close();

    }
}
