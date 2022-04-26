package com.hzy.nio.writeAction;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author hzy
 */
public class WriteServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        Selector selector = Selector.open();
        //设置非阻塞模式
        ssc.configureBlocking(false);
        //把serverSocket 注册进selector，并且关注连接事件。
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //指定server的端口
        ssc.bind(new InetSocketAddress(6618));

        while(true){
            selector.select();
            //获取监听到的事件集合
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                //当还有需要处理的事件时，就用迭代器进行循环 并删除已处理的事件key
                SelectionKey key = iterator.next();
                iterator.remove();
                //是连接事件
                if (key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);


                    //试着向客户端写数据，这里写了很大的数据，是为了测试buffer容量的允许范围
                    StringBuilder builder = new StringBuilder();
                    for (int i=0;i<100000000;i++){
                        builder.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(builder.toString());
                    //当buffer里还有内容没有写完
                    while (buffer.hasRemaining()){
                        //拿到本次成功写入的字节数
                        int count = sc.write(buffer);
                        System.out.println(count);
                    }

                }
            }
        }

    }
}
