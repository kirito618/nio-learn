package com.hzy.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

//字符串转成ByteBuffer
public class ByteBufferTest2 {

    public static void main(String[] args) {
        //把字符串塞入ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put("hello".getBytes(StandardCharsets.UTF_8));


        //切换读模式，position指针移动到0，limit指针移动到最大可读处
        buffer.flip();
        //把buffer中的内容转成String
        String str = StandardCharsets.UTF_8.decode(buffer).toString();

        System.out.println(str);
    }
}
