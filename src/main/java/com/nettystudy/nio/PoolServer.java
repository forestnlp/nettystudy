package com.nettystudy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolServer {

    ExecutorService pool = Executors.newCachedThreadPool();

    private Selector selector;

    public static void main(String[] args) {
        PoolServer server = new PoolServer();
        server.initServer(8888);
        server.listen();
    }

    private void initServer(int port) {
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            this.selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if(key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel)key.channel();
                        SocketChannel channel=server.accept();
                        channel.configureBlocking(false);
                        channel.register(this.selector,SelectionKey.OP_READ);
                    } else if(key.isReadable()) {
                        key.interestOps(key.interestOps()&(~SelectionKey.OP_READ));
                        pool.execute(new ThreadHandler(key));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
