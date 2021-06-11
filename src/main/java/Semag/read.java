/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author xianp
 */
public class read implements Runnable {
    
    private DataInputStream dis;
    Thread t;
    ChatWindow gui;

    public read(DataInputStream dis, ChatWindow gui) {
        this.gui = gui;
        this.dis = dis;
        t = new Thread(this);
        t.start(); // Starting the thread
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                // read the message sent to this Client
                String msg = dis.readUTF();
                if (msg.equals("logout")) {
                    this.t.interrupt();
                }
                gui.after_read(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
