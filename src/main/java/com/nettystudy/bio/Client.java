package com.nettystudy.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class Client {

    public static void main(String[] args) throws IOException {

        Random random = new Random();
        int nounce = random.nextInt(1000);
        String msg = "Hi,I'm bio client~"+String.valueOf(nounce);
        Socket s = new Socket("127.0.0.1",8888);
        s.getOutputStream().write(msg.getBytes());
        s.getOutputStream().flush();

        System.out.println("client msg :"+msg+" already sended!");
        byte[] bytes = new byte[1024];
        int len = s.getInputStream().read(bytes);
        System.out.println(new String(bytes,0,len));
        s.close();
    }

}
