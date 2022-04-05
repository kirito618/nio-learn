package com.hzy.nio.blockedModel;

import com.sun.org.slf4j.internal.Logger;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
*  Server服务端
 * @author hzy
**/
public class Server {
    public static void main(String[] args) throws IOException {
        //1、创建一个selector监听器
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //设置非阻塞模式
        ssc.configureBlocking(false);

        //2、把channel和selector建立联系（也就是所谓的 把channel进行 ”注册“）
        //ops表示我们注册到selector之后 希望监听这个serverSocketChannel的什么事件，0就表示啥都不监听
        //每向selector中注册一个通道，就会给他创建一个 SelectionKey 并返回这个key，里面存储着 要监听这个通道的什么活动 并且可以通过这个key找到这个通道
        SelectionKey sscKey = ssc.register(selector,0,null);
        //设置对于这个key的通道 要监听什么事件 这里是监听连接事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        //给当前ServerSocket绑定一个端口地址
        ssc.bind(new InetSocketAddress(8787));

        while(true){
            //3、使用select方法 让selector进入监听状态 如果有被selector监听的事件发生 则继续执行 否则一直堵塞
            selector.select();
            //4、处理这个事件 selectionKeys 是一个Set集合 里面存着所有注册过的通道的key
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                //这个Keys集合里面存着所有监听事件的key，而这些事件的类型会不一样，应该对不同的事件的key 进行不同的处理
                SelectionKey key = iterator.next();
                //Keys集合无法自己删除事件的key，如果不删除，事件无法二次处理，就会导致拿不到key里面的内容而报空指针异常
                iterator.remove();
                if (key.isAcceptable()){
                    //是accept事件
                    System.out.println("serverKey : " + key.toString());
                    //得到当前这个 key 对应的channel 因为我这里只是服务端 所以是ServerSocketChannel
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //这时候就可以安全的调用accept方法来获取连接了（因为上面已经确定了 有人要跟我进行连接 所以accept不会堵塞导致影响后续的代码执行）
                    SocketChannel sc = channel.accept();
                    //设置非阻塞状态
                    sc.configureBlocking(false);
                    //把建立的连接SocketChannel通道也注册到selector中去
                    //这个SelectionKey就专门对应SocketChannel中发生的事件
                    SelectionKey socketKey = sc.register(selector,0,null);
                    //对于这个channel 只关注读事件
                    socketKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("连接上了！client：" + sc.getRemoteAddress());
                }else if (key.isReadable()) {
                    //是可读事件
                    //拿到出发这个事件的那个channel
                    SocketChannel selectableChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    //把数据从channel读，向buffer写
                    selectableChannel.read(buffer);
                    System.out.println(selectableChannel.getRemoteAddress() + "说: " + new String(buffer.array()));
                    //调转buffer读写模式
                    buffer.flip();
                }
            }
        }
    }
}
