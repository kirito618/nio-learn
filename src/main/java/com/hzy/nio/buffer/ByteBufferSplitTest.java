package com.hzy.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBufferSplitTest {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put("今天再见\n明天你好".getBytes(StandardCharsets.UTF_8));
        split(buffer);
        System.out.println(buffer.toString());
    }

    //把buffer的内容进行截取，相当于是在截取消息
    public static void split(ByteBuffer buffer){
        //调转成读模式
        buffer.flip();
        for (int i=0;i<buffer.limit();i++){
            if (buffer.get(i)=='\n'){
                //读到换行符，说明这里阔以截取了
                //截取的长度：i + 1代表当前读到第几个字符了，由于每次碰到分隔符，就是一个完整句子
                //而完整句子的开始下标是 position 所以减去position
                int length = i + 1 - buffer.position();
                //把分割后的完整消息存入到新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);

                //从buffer中读，向target中写入
                for (int j=0;j<length;j++){
                    //由于get方法没有传入i 所以会改变position指针
                    target.put(buffer.get());
                }
            }
        }
        //HeapByteBuffer 是堆内存缓冲区
        System.out.println(buffer.toString());

        //切换读写模式，compact可以把当前未读到的内容向左进行覆盖
        buffer.compact();
    }
}
