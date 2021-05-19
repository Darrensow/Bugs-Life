/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xianp
 */
public class clienthander implements Runnable {

    Scanner scn = new Scanner(System.in);
    private String name;
    private int code;
    private String position;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
    server server = new server();

    // constructor 
    public clienthander(Socket s, String name, DataInputStream dis, DataOutputStream dos, int code, String position) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin = true;
        this.code = code;
        this.position = position;
    }

    @Override
    public void run() {

        String received = null;
        boolean readname = false;
        try {
            if (position.equals("owner")) {
                this.dos.writeUTF("you " + this.name + " are entered group. This  is the group code: " + this.code);
            } else {
                this.dos.writeUTF("you " + this.name + " are entered group.");
            }

        } catch (IOException ex) {
            Logger.getLogger(clienthander.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                // receive the string 
                if (readname == true) {
                    received = dis.readUTF();
                    if (received.equals("logout")) {
                        dos.writeUTF(this.name + " you are logout from the group");
                        for (int i = 0; i < server.a.size(); i++) {
                            if (readname = true && server.a.get(i).isloggedin == true && server.a.get(i).code == (this.code) && !server.a.get(i).name.equals(this.name)) {
                                server.a.get(i).dos.writeUTF(this.name + " are logout from this group");
                            }
                        }
                        this.isloggedin = false;
//                        this.s.close();
                        break;
                    }
                }
//                System.out.println(received);

                // break the string into message and recipient part 
                // search for the recipient in the connected devices list. 
                // ar is the vector storing client of active users
                for (int j = 0; j < server.a.size(); j++) {//&& !server.a.get(j).equals(this)
                    if (readname == true && server.a.get(j).isloggedin == true && server.a.get(j).code == (this.code)) {
                        server.a.get(j).dos.writeUTF(this.name + " : " + received);
                    } else if (server.a.get(j).code == (this.code) && !server.a.get(j).equals(this)) {
                        server.a.get(j).dos.writeUTF(this.name + " entered group");
                    }
                }
                readname = true;
//                for (ClientHandler mc : S3.ar) {
//                    // if the recipient is found, write on its 
//                    // output stream 
//                    if (mc.name.equals(towho) && mc.isloggedin == true) {
//                        mc.dos.writeUTF(this.name + " : " + information);
//                        break;
//                    }
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        try {
////             closing resources 
//            this.dis.close();
//            this.dos.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
