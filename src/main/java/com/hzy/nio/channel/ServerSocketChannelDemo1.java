package com.hzy.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerSocketChannelDemo1 {

    public static void main(String[] args) throws Exception {
        //设置一个端口号
        int port = 8888;

        //创建Buffer
        ByteBuffer buffer = ByteBuffer.wrap("hello hzy".getBytes(StandardCharsets.UTF_8));

        //使用ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //绑定端口号
        ssc.bind(new InetSocketAddress(port));

        //设置非阻塞模式,若传参是false那么就是非阻塞，传参是true就是阻塞
        ssc.configureBlocking(true);

        //没有使用Selector  这里就是用while循环进行监听连接
        while (true){
            System.out.println("Waiting for connections");
            //accept方法就是启动监听了，会返回一个SocketChannel对象，如果是null就说明当前没有新建的连接
            SocketChannel sc = ssc.accept();
            if (sc==null){
                //当前没有新的连接
                System.out.println("null");
                Thread.sleep(2000);
            }else{
                //有连接传入
                System.out.println("connection from: " + sc.socket().getRemoteSocketAddress());
                //rewind()方法能够将buffer指针归零，也就是从头开始了
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }
}
