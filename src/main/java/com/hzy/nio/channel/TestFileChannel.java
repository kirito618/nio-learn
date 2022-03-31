package com.hzy.nio.channel;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 利用FileChannel进行文件之间的数据传输
 * */
public class TestFileChannel {

    public static void main(String[] args) {

        try{
            //创建两个随意读写文件，并获取他们的FileChannel
            RandomAccessFile from = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\from.txt","rw");
            RandomAccessFile to = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\to.txt","rw");
            FileChannel fromChannel = from.getChannel();
            FileChannel toChannel = to.getChannel();

            //transferTo() 方法底层使用了操作系统的零拷贝进行优化，效率高
            //使用 transferTo() 方法进行两个文件之间的传输
            //三个参数： 传输的起始位置，要传多少个，往哪传（另一个通道）
            fromChannel.transferTo(0,fromChannel.size(),toChannel);

            //使用transferFrom() 方法，从另一个文件传输内容到当前channel
            fromChannel.transferFrom(toChannel,0,toChannel.size());

            fromChannel.close();
            toChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
