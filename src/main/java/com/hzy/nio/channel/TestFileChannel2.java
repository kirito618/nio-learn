package com.hzy.nio.channel;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class TestFileChannel2 {


    public static void main(String[] args) throws Exception{
        RandomAccessFile file1 = new RandomAccessFile("src\\main\\resources\\static\\from.txt","rw");
        RandomAccessFile file2 = new RandomAccessFile("src\\main\\resources\\static\\to.txt","rw");
        FileChannel from = file1.getChannel();
        FileChannel to = file2.getChannel();
        long size = from.size();
        //left用来记录当前读了多少(size - left)就是读取的起始位置 一开始是0
        for (long left = size;left>0;){
            //如果文件过大，那么每次读取都只能读取2g，剩余的部分读取不到，所以利用循环进行多次读取
            //每次的开始位置是 size - left(表示已读取了多少) size是整个from的大小(每次都去读最大的量)
            System.out.println("position: " + (size - left)+ ", 剩余left: " + left);
            left -= from.transferTo((size - left),left,to);
        }
    }
}
