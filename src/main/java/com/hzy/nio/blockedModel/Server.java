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
                //HeapByteBuffer 是堆内存缓冲区
            }
        }
        //切换读写模式，compact可以把当前未读到的内容向左进行覆盖
        buffer.compact();
    }




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
                    //第三个参数称为附件，将和我们的channel进行对应 关联到selectionKey 对于消息的处理，我们每个channel都应该有自己的buffer，所以buffer可以作为附件
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey socketKey = sc.register(selector,0,buffer);
                    //对于这个channel 只关注读事件
                    socketKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("连接上了！client：" + sc.getRemoteAddress());
                }else if (key.isReadable()) {
                    try{
                        //是可读事件
                        //拿到出发这个事件的那个channel
                        SocketChannel selectableChannel = (SocketChannel) key.channel();
                        //拿到注册时 作为附件的那个ByteBuffer
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        //把数据从channel读，向buffer写
                        int read = selectableChannel.read(buffer);
                        //如果是不正常的断开，那么selectableChannel这个对象就不存在了，无法向buffer写入内容，会报IO异常
                        //这里是处理 正常断开时 的操作，如果是正常断开 那么这个channel里就没有内容了，read会返回-1
                        if (read == -1){
                            key.cancel();
                        }else{
                            //截取消息
                            split(buffer);
                            if (buffer.position() == buffer.limit()){
                                //消息太长了，读满了buffer，仍然有剩余，就新开辟空间，创建更大(2倍大小)的buffer
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity()*2);
                                //调转buffer读写模式，变成读模式
                                buffer.flip();
                                //从buffer读 向newBuffer写,把上次读入的内容复制到新的buffer中去
                                newBuffer.put(buffer);
                                //更换key所绑定的那个buffer，换成新的
                                key.attach(newBuffer);
                            }
                            //调转buffer读写模式
                            buffer.flip();
                            System.out.println(new String(buffer.array()));
                        }
                    }catch (IOException e){
                        System.out.println("有客户端断开连接了...");
                        //删除这个key，保证下一次进循环时 能够让selector正常工作
                        //如果没删，只是捕捉异常的话，会死循环的，因为这个key已经读不到东西了，一读就异常，然后被catch，相当于没有被做处理，那么下次还会轮到这个key，死循环
                        key.cancel();
                    }

                }
            }
        }
    }
}
