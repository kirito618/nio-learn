package com.hzy.nio.writeAction;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriterClient {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",6618));

        int count = 0;
        //客户端接收数据
        while(true){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //记录此次读取到的字节数
            count += sc.read(buffer);
            System.out.println(count);
            //每次的读完成之后  就清空一下buffer
            buffer.clear();
        }
    }
}
