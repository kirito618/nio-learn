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
        //设置为非阻塞状态
        //如果设置成了非阻塞效果，那么本应该阻塞的accept和read都不会阻塞，但是accept返回的SocketChannel就是null了
        serverSocketChannel.configureBlocking(false);

        //给ServerSocket绑定一个端口
        serverSocketChannel.bind(new InetSocketAddress(8787));

        //记录当前已经建立连接的socketChannel
        List<SocketChannel> channels = new ArrayList<>();

        //下面的accept和read方法都是阻塞的，所以当同时有多个连接的建立时，一个连接的阻塞会影响到其他的连接不能继续执行
        while(true){
            //accept() 和传统Socket一样是阻塞的 如果没有建立连接 代码就不会往下走 会一直堵在accept这一行
            SocketChannel sc = serverSocketChannel.accept();
            if (sc != null){
                System.out.println("连接成功" + sc.getRemoteAddress());
                channels.add(sc);
                //要把获得的socketChannel也设置成非阻塞的，才能保证read方法也不阻塞
                sc.configureBlocking(false);
            }
            for (SocketChannel sc1:channels){
                if (!sc1.isOpen()){
                    //循环删除已经断开连接的SocketChannel
                    channels.remove(sc1);
                    continue;
                }
                //接收客户端发来的消息 写到buffer里去
                //同时这个read方法也是阻塞的，在对方没有发来消息之前，下面的代码也不会执行
                int code = sc1.read(buffer);
                if (code > 0){
                    //如果没有读取到内容 那么read返回值就是0
                    System.out.println(buffer.toString());
                    //调转读写模式
                    buffer.flip();
                    buffer.clear();
                }
            }
        }
    }
}
