package com.hzy.nio.channel;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel 学习代码一 : 从Buffer中读取数据
 * */
public class FileChannelDemo1 {

    //FileChannel读取数据到buffer中
    public static void main(String[] args) throws IOException {
        //创建Channel
        //第一个参数是文件路径，第二个参数是权限，rw表示即可读又可写
        RandomAccessFile file = new RandomAccessFile("f:\\java\\NIO-learn\\src\\main\\resources\\static\\nio.txt","rw");
        FileChannel channel = file.getChannel();


        //创建Buffer
        //allocate()里面写入创建大小
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //读取数据到buffer
        //如果read返回值为-1  表示读取结束了
        int bytesRead = channel.read(buf);
        while(bytesRead!=-1){
            System.out.println("读取了："+bytesRead);
            //反转读写模式，这里是切换成写
            buf.flip();
            while(buf.hasRemaining()){
                //这里是取得缓冲区内的内容
                System.out.println((char)buf.get());
            }
            //清空缓冲区
            buf.clear();
            bytesRead = channel.read(buf);
        }
        file.close();
        System.out.println("结束了");

    }
}
