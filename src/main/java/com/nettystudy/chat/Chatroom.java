package com.nettystudy.chat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Chatroom extends Frame{

    TextArea ta = new TextArea();
    TextField tf = new TextField();

    Client client ;

    public static final Chatroom INSTANCE = new Chatroom();

    private Chatroom() {
        this.setSize(600,400);
        this.setLocation(20,80);
        this.add(ta,BorderLayout.CENTER);
        this.add(tf,BorderLayout.SOUTH);
        this.setTitle("chatroom");
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.send(tf.getText());
                tf.setText("");
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeConnect();
                System.exit(0);
            }
        });
    }

    private void closeConnect() {
        client.send("886");
    }

    public void connectServer(){
        client = new Client();
        client.connect();
    }

    public static void main(String[] args) {
        Chatroom frame = Chatroom.INSTANCE ;
        frame.setVisible(true);
        frame.connectServer();
    }

    public void updateText(String s) {
        Chatroom.INSTANCE.ta.setText(ta.getText()+System.getProperty("line.separator")+s);
    }
}
