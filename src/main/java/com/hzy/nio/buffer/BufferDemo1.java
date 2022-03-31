package com.hzy.nio.buffer;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo1 {

    @Test
    public void buffer01() throws Exception{
        //打开一个Channel
        RandomAccessFile accessFile = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\nio3.txt","rw");
        FileChannel channel = accessFile.getChannel();

        //创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //读取Buffer中的数据
        int bytesRead = channel.read(buffer);
        //!=-1说明还没有读到结尾
        while(bytesRead!=-1){
            //调转读写模式
            buffer.flip();

            while(buffer.hasRemaining()){
                System.out.println((char)buffer.get());
            }
            buffer.clear();
            bytesRead = channel.read(buffer);
        }
        accessFile.close();
    }



    @Test
    public void Buffer02() throws Exception{
        //创建IntBuffer,长度8个字节
        IntBuffer buffer = IntBuffer.allocate(8);
        //buffer.capacity()表示这个buffer的大小
        for (int i=0;i<buffer.capacity();i++){
            //把数据推入到缓冲区去
            buffer.put(i+1);
        }

        //重置缓冲区
        buffer.flip();

        //获取
        while(buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
