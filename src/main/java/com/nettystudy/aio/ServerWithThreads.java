package com.nettystudy.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWithThreads {

    public static void main(String[] args) throws IOException, InterruptedException {

        ExecutorService pool = Executors.newCachedThreadPool();
        AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withCachedThreadPool(pool, 50);

        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup).bind(new InetSocketAddress(8888));

        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel client, Object attachment) {
                serverSocketChannel.accept(null,this);
                try {
                    System.out.println(client.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            System.out.println("aio server got a msg:"+new String(attachment.array(),0,result));
                            client.write(ByteBuffer.wrap("I am aio server ,respect !".getBytes()));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {

                        }
                    });
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });

        while (true){
            Thread.sleep(500);
        }
    }

}
