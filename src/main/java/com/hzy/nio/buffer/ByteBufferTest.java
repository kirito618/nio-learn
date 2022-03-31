package com.hzy.nio.buffer;


import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferTest {

    public static void main(String[] args) {
        try{
            RandomAccessFile file = new RandomAccessFile("src//main//resources//static//nio.txt","rw");
            FileChannel channel = file.getChannel();
            //设置缓冲区容量
            ByteBuffer buffer = ByteBuffer.allocate(3);


            while(true){
                //从channel读取数据 并将数据写入到buffer中去,返回值为读取的字节数  若返回-1  就是读到最后了
                int len = channel.read(buffer);
                if (len==-1){
                    break;
                }
                //打印buffer中的内容
                //切换至读模式
                buffer.flip();
                //是否还有内容
                while(buffer.hasRemaining()){
                    byte b = buffer.get();
                    System.out.println((char)b);
                }
                //把buffer切换成写模式
                buffer.clear();
            }

        }catch (Exception e){

        }


    }
}
