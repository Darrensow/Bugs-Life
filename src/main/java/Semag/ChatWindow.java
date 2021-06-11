/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xianp
 */
public class ChatWindow implements ActionListener, MouseListener {

    private String str = "";
    private boolean entered = false;
    private JLabel title = new JLabel();
    private JFrame frame = new JFrame();
    private JTextArea keyin_place = new JTextArea();
    private JTextPane displaytext = new JTextPane();
    private JButton button = new JButton("submit");
    private Border border = BorderFactory.createLineBorder(Color.yellow);
    private char bold = '*'; // * _ ~ $ @ # ^
    private char italic = '_';
    private char cancel = '~';
    private char underline = '$';
    private char highlight = '@';
    private char belowletter = '#';
    private char aboveletter = '^';
    private SimpleAttributeSet word_style = new SimpleAttributeSet();
    private ArrayList<Character> special = new ArrayList<>(Arrays.asList(bold, italic, cancel, underline, highlight, belowletter, aboveletter));
    private Document doc = displaytext.getStyledDocument();
    DataInputStream dis;
    DataOutputStream dos;
    read read;


    public ChatWindow(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        read = new read(dis, this);
        ImageIcon app_icon = new ImageIcon("whatapps icon.jpg");
        frame.setLayout(null);
        frame.setTitle("Chat window");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    dos.writeUTF("logout");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        frame.setIconImage(app_icon.getImage());
        frame.setResizable(false);
        frame.setSize(690, 700);

        title.setBackground(Color.red);
        title.setBorder(border);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setOpaque(true);
        title.setBounds(0, 0, 675, 50);
        title.setText("Group Chat");
        title.setFont(new Font("Serif", Font.PLAIN, 35));

        displaytext.setEditable(false);
        displaytext.setVisible(true);
        displaytext.setBounds(0, 0, 675, 500);

        JScrollPane scrollPane = new JScrollPane(displaytext);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 50, 675, 500);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        keyin_place.setEditable(true);
        keyin_place.setLineWrap(true);
        keyin_place.setWrapStyleWord(true);
        keyin_place.setText("Type a message");
        keyin_place.addMouseListener(this);

        JScrollPane scroll_keyin_place = new JScrollPane(keyin_place);
        scroll_keyin_place.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_keyin_place.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_keyin_place.setBounds(0, 550, 600, 100);

        button.addActionListener((ActionListener) this);
        button.setBounds(600, 600, 75, 30);
        frame.add(title);
        frame.add(button);
        frame.add(scroll_keyin_place);
        frame.setVisible(true);

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            entered = false;
            str = keyin_place.getText();
            try {
                dos.writeUTF(str);
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void after_read(String str) {
        String[] line = str.split("\n");
        for (int i = 0; i < line.length; i++) {
            try {
                tokenization(line[i]);
            } catch (BadLocationException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        keyin_place.setText("Type a message");
    }

    public void mouseClicked(MouseEvent e) { //when mouse clivk
        if (entered == false) {
            entered = true;
            keyin_place.setText("");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setbold() {
        StyleConstants.setBold(word_style, true);
    }

    public void setitalic() {
        StyleConstants.setItalic(word_style, true);  // set type
    }

    public void setunderline() {
        StyleConstants.setUnderline(word_style, true);
    }

    public void setcancel() {
        StyleConstants.setStrikeThrough(word_style, true);
    }

    public void sethighlight() {
        StyleConstants.setForeground(word_style, Color.red);      // set color
        StyleConstants.setBackground(word_style, Color.blue);
    }

    public void setbelowletter() {
        StyleConstants.setSubscript(word_style, true);
    }

    public void setaboveletter() {
        StyleConstants.setSuperscript(word_style, true);
    }

    public void style(String token) throws BadLocationException {
        for (int i = 0; i < token.length(); i++) {
            boolean endofsetup = true;
            if (token.charAt(i) == bold) {
                endofsetup = false;
                setbold();
            }
            if (token.charAt(i) == italic) {
                endofsetup = false;
                setitalic();
            }
            if (token.charAt(i) == cancel) {
                endofsetup = false;
                setcancel();
            }
            if (token.charAt(i) == underline) {
                endofsetup = false;
                setunderline();
            }
            if (token.charAt(i) == highlight) {
                endofsetup = false;
                sethighlight();
            }
            if (token.charAt(i) == belowletter) {
                endofsetup = false;
                setbelowletter();
            }
            if (token.charAt(i) == aboveletter) {
                endofsetup = false;
                setaboveletter();
            }
            if (endofsetup == true) {
//                System.out.println(token.substring(i, token.length() - i));
                print(token.substring(i, token.length() - i));
                break;
            }
        }

    }

    public void tokenization(String token) throws BadLocationException {
        if (token.length() == 0) {
            token = " ";
        }
        String[] tokens = token.split(" ");

        ChatQueue<String> tokens_queue = new ChatQueue<>();
        String temp = "";
        String special_header = "";
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].length() > 0 && !special.contains(tokens[i].charAt(0)) && temp.equals("")) {
                tokens_queue.enqueue(" " + tokens[i] + " ");
            } else if (tokens[i].equals("\n")) {
                tokens_queue.enqueue(temp + tokens[i]);
                temp = "";
                special_header = "";
            } else {
                if (special_header.equals("")) {
                    for (int j = 0; j < tokens[i].length(); j++) {
                        if (special.contains(tokens[i].charAt(j))) {
                            special_header += tokens[i].charAt(j);
                        } else {
                            break;
                        }
                    }
                }
                int check = 0;
                for (int j = tokens[i].length() - 1; j >= 0; j--) {
                    if (tokens[i].charAt(j) == special_header.charAt(check)) {
                        check++;
                        if (check >= special_header.length()) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (check == special_header.length()) {
                    tokens_queue.enqueue(temp + " " + tokens[i] + " ");
                    temp = "";
                    special_header = "";
                } else {
                    temp += " " + tokens[i] + " ";
                }
            }
        }

        while (!tokens_queue.isEmpty()) {
            String pass = tokens_queue.dequeue();
            if (pass.length() >= 3) {
                style(pass.substring(1, pass.length() - 1));
            } else {
                if (pass.length() == 0) {
                    style(" ");
                }
                style(pass);
            }
        }
        print("\n");

    }

    private void print(String substring) throws BadLocationException {
        doc.insertString(doc.getLength(), substring + " ", word_style);
        word_style = new SimpleAttributeSet();
    }
}
