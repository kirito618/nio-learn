package com.hzy.nio.blockedModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client implements Runnable{
    @Override
    public void run() {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("localhost",8787));
            Scanner scanner = new Scanner(System.in);
            while (true){
                String msg = scanner.nextLine();
                sc.write(Charset.defaultCharset().encode(msg));
//                sc.write(Charset.defaultCharset().encode("fuck!\nwhat are u talking about!fuck u /n"));
//                sc.write(Charset.defaultCharset().encode("fuck!\n"));
//                if ("bye".equals(msg)){
//                    //输入 bye 之后断开连接
//                    sc.close();
//                    break;
//                }
            }
//            System.out.println("waiting.......");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        new Thread(new Client()).start();

    }
}
