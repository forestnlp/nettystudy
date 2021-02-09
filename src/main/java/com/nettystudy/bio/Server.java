package com.nettystudy.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8888);
        System.out.println("server started!");
        while (true) {
            Socket s = ss.accept();
            new Thread(()->{
                handle(s);
            }).start();
        }

    }

    private static void handle(Socket s) {
        byte[] bytes= new byte[1024];
        try {
            int len = s.getInputStream().read(bytes);
            System.out.println("Server accept a msg:"+new String(bytes,0,len));

            s.getOutputStream().write("welcome!".getBytes());
            s.getOutputStream().flush();
            s.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
