package com.hzy.nio.blockedModel;

import com.sun.org.slf4j.internal.Logger;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
*  Server服务端
 * @author hzy
**/
public class Server {
    public static void main(String[] args) throws IOException {
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);

        //创建一个ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //给ServerSocket绑定一个端口
        serverSocketChannel.bind(new InetSocketAddress(8787));

        //记录当前已经建立连接的socketChannel
        List<SocketChannel> channels = new ArrayList<>();

        //下面的accept和read方法都是阻塞的，所以当同时有多个连接的建立时，一个连接的阻塞会影响到其他的连接不能继续执行
        while(true){
            //阻塞式建立连接
            System.out.println("正在连接中");
            //accept() 和传统Socket一样是阻塞的 如果没有建立连接 代码就不会往下走 会一直堵在accept这一行
            SocketChannel sc = serverSocketChannel.accept();
            System.out.println("连接成功" + sc.getRemoteAddress());
            channels.add(sc);
            for (SocketChannel sc1:channels){
                //接收客户端发来的消息 写到buffer里去
                //同时这个read方法也是阻塞的，在对方没有发来消息之前，下面的代码也不会执行
                sc1.read(buffer);
                System.out.println(new String(buffer.array()));
                //调转读写模式
                buffer.flip();
                buffer.clear();
            }
        }
    }
}
