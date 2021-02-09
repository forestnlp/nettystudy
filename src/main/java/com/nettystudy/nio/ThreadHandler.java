package com.nettystudy.nio;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ThreadHandler implements Runnable {

    private SelectionKey key;

    @Override
    public void run() {
        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            int size = 0;
            while ((size = channel.read(buffer))>0){
                buffer.flip();
                baos.write(buffer.array(),0,size);
                System.out.println("server get a msg:"+new String(buffer.array()));
                buffer.clear();
            }
            baos.close();

            byte[] bytes = baos.toByteArray();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
            if(size==-1) {
                channel.close();
            }
            else {
                key.interestOps(key.interestOps()|SelectionKey.OP_READ);
                key.selector().wakeup();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ThreadHandler(SelectionKey key) {
        this.key = key;
    }
}
