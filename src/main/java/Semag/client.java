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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author xianp
 */
public class client implements Serializable {

    final static int ServerPort = 1234;

    public client(People people) throws UnknownHostException, IOException {
        Scanner scn = new Scanner(System.in);

        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection 
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams 
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        dos.writeUTF(people.getName());
        String[] responde = {"Create New Room", "Enter Room"};
        int action = JOptionPane.showOptionDialog(null, "Chat Room", "title", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, responde, 0);

        if (action == 0) {
            dos.writeInt(-1);
        } else {
            String code_string = JOptionPane.showInputDialog("Enter code");
            int code = Integer.parseInt(code_string);
            dos.writeInt(code);
        }
        new chat_window(dis, dos);

    }

}