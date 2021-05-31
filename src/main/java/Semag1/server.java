/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author xianp
 */
public class server implements Serializable {

    protected static ArrayList<clienthander> a = new ArrayList<>();
    private static ArrayList<Integer> code_list = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(1234);
        Socket s;
        while (true) {
            s = ss.accept();
//            System.out.println("New client request received : " + s);

            // obtain input and output streams 
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

//            System.out.println("Creating a new handler for this client...");

            // Create a new handler object for handling this request. 
            String name = dis.readUTF();
            int code = dis.readInt();
            clienthander mtch;
            if (code == -1) {
                code = new Random().nextInt(999999 - 100000 + 1) + 100000;
                while (code_list.contains(code)) {
                    code = new Random().nextInt(999999 - 100000 + 1) + 100000;
                }
                mtch = new clienthander(s, "#" + name, dis, dos, code, "owner");
            } else {
                mtch = new clienthander(s, "#" + name, dis, dos, code, "member");
            }
//            System.out.println(code);

            // Create a new Thread with this object. 
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // add this client to active clients list 
            a.add(mtch);
            // start the thread. 
            t.start();
        }
    }
}
