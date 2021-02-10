package com.nettystudy.chat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Chatroom extends Frame{

    TextArea ta = new TextArea();
    TextField tf = new TextField();

    public Chatroom() {
        this.setSize(600,400);
        this.setLocation(20,80);
        this.add(ta,BorderLayout.CENTER);
        this.add(tf,BorderLayout.SOUTH);
        this.setTitle("chatroom");
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.setText(ta.getText()+"\n"+tf.getText());
                tf.setText("");
            }
        });
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new Chatroom();
    }
}
