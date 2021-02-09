package com.nettystudy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(8888));
        ssc.configureBlocking(false);

        System.out.println("nio server starts!");
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                handle(key);
            }
        }

    }

    private static void handle(SelectionKey key) {
        if(key.isAcceptable()){
            try{
                ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                SocketChannel socketChannel = channel.accept();
                socketChannel.configureBlocking(false);
                //产生了一个新的channel，监听读事件
                socketChannel.register(key.selector(),SelectionKey.OP_READ);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else if(key.isReadable()) {
            SocketChannel sc = null;
            try{
                sc = (SocketChannel)key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(512);
                buffer.clear();
                int len = sc.read(buffer);
                if(len!=-1){
                    System.out.println(new String(buffer.array(),0,len));
                }
                ByteBuffer wrap = ByteBuffer.wrap("hello! I am Server".getBytes());
                sc.write(wrap);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(sc!=null)
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
