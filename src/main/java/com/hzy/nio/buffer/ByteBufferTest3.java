package com.hzy.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class ByteBufferTest3 {

    public static void main(String[] args) {
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("fuck");
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("no");
        ByteBuffer buffer3 = StandardCharsets.UTF_8.encode("yes!");

        try {
            FileChannel channel = new RandomAccessFile("src//main//resources//static//nio4.txt","rw").getChannel();
            //将多个buffer集中起来，写入到channel中去
            channel.write(new ByteBuffer[]{buffer1,buffer2,buffer3});
        }catch (Exception e){

        }
    }
}
