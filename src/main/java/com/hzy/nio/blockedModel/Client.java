package com.hzy.nio.blockedModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8787));
        Scanner scanner = new Scanner(System.in);
        sc.write(Charset.defaultCharset().encode(scanner.nextLine()));
        System.out.println("waiting.......");

    }
}
