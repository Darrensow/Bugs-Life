package Semag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class User implements ActionListener {

    private PeopleADT people_array = new PeopleADT();

    private int ID = 0;

    //gui part
    JPanel backgroud_panel = new JPanel();
    JPanel login_panel = new JPanel();
    JPanel register_panel = new JPanel();
    JButton register = new JButton();
    JButton login = new JButton();
    JButton login_in = new JButton();
    JButton register_button = new JButton();
    JButton next = new JButton();
    JButton verify_button = new JButton();
    JTextField login_username = new JTextField();
    JLabel login_label = new JLabel();
    JLabel register_label = new JLabel();
    JLabel verify_label = new JLabel();
    JTextField gmail_text = new JTextField();
    JTextField verify_code_text = new JTextField();
    JTextField register_username_text = new JTextField();
    JFrame frame = new JFrame();

    JPasswordField login_pass_text = new JPasswordField();
    JPasswordField register_pass_text = new JPasswordField();
    JPasswordField reg_confirm_pass_text = new JPasswordField();
    JLabel login_pass_label = new JLabel();
    JLabel register_pass_text_label = new JLabel();
    JLabel reg_confirm_pass_text_label = new JLabel();
    JLabel login_user_label = new JLabel();
    JLabel register_user_label = new JLabel();

    String mail = "";
    String verified;
    String num;
    Password password_obj;
    People people_obj = null;
    boolean can_return = false;

    public User() {
    }

    public People register() throws InterruptedException {
        people_obj = null;
        window_setup();
        sleep();
        frame.setVisible(false);
        return people_obj;
    }

    public void sleep() throws InterruptedException {
        Thread.sleep(1000);
        if (can_return) {
            return;
        } else {
            sleep();
        }
    }

    public void popwindow(String title, String content) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.WARNING_MESSAGE);
    }

    public void send_email(String email) {
        String title = "Vertified code by Doge";
        password_obj = new Password();
        verified = password_obj.getPass();
        String content = "Hi this is the vertified code : " + verified;
        GmailSender obj = new GmailSender(email, title, content);
        obj.send();
    }

    public int getindex(String name) {
        System.out.println(people_array.size() + " size");
        for (int i = 0; i < people_array.size(); i++) {
            System.out.println(people_array.get(i).getName());
            if (people_array.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    // -- Getter and setter methods --
    public PeopleADT getPeople_array() {
        return people_array;
    }

    public void setPeople_array(PeopleADT people_array) {
        this.people_array = people_array;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void window_setup() {
        //setup frame

        ImageIcon konoha = new ImageIcon("doge.png");
        frame.setLayout(null);
        frame.setTitle("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(konoha.getImage());
        frame.setResizable(false);
        frame.setSize(1350, 730);
        frame.setLayout(null);
        frame.setVisible(true);

        //set up back groud picture
        ImageIcon image = new ImageIcon("background.png");
        JLabel label = new JLabel(image);
        label.setBounds(0, 0, 1350, 690);
        label.setVisible(true);

        //builf login panel
        login_panel.setBackground(Color.black);
        login_panel.setOpaque(true);
        login_panel.setLayout(null);
        login_panel.setBounds(500, 200, 300, 300);
        login_panel.setVisible(false);

//build login user label 
        login_user_label.setText("Username :");
        login_user_label.setBounds(25, 75, 80, 50);
        login_user_label.setVisible(true);
        login_user_label.setForeground(Color.white);
        //build login username text

        login_username.setText("Enter username");
        login_username.setBounds(105, 75, 150, 50);
        login_username.setEditable(true);
        login_username.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (login_username.getText().equals("Enter username")) {
                    login_username.setText("");
                }
            }
        });
        login_username.setVisible(true);

//build login_password label
        login_pass_label.setText("Password : ");
        login_pass_label.setVisible(true);
        login_pass_label.setBounds(25, 175, 80, 50);
        login_pass_label.setForeground(Color.WHITE);
//build login password text
        login_pass_text.setBounds(105, 175, 150, 50);
        login_pass_text.setVisible(true);

        //build login button
        login_in.setText("Login");
        login_in.setVisible(true);
        login_in.addActionListener(this);
        login_in.setBounds(105, 235, 100, 30);
//build the title of login panel
        login_label.setBounds(0, 0, 300, 35);
        login_label.setText("Login");
        login_label.setHorizontalAlignment(0);
        login_label.setVerticalAlignment(0);
        login_label.setForeground(Color.red);
        login_label.setFont(new Font("Serif", Font.BOLD, 30));
//add component to login panel
        login_panel.add(login_username);
        login_panel.add(login_user_label);
        login_panel.add(login_pass_text);
        login_panel.add(login_pass_label);
        login_panel.add(login_label);
        login_panel.add(login_in);
//build register panel
        register_panel.setBackground(Color.black);
        register_panel.setOpaque(true);
        register_panel.setLayout(null);
        register_panel.setBounds(500, 200, 300, 400);
        register_panel.setVisible(false);
//build enter gmail text 
        gmail_text.setText("Enter Gmail");
        gmail_text.setBounds(25, 100, 250, 50);
        gmail_text.setEditable(true);
        gmail_text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (gmail_text.getText().equals("Enter Gmail")) {
                    gmail_text.setText("");
                }
            }
        });
        gmail_text.setVisible(true);
//build next button
        next.setText("Next");
        next.setVisible(true);
        next.addActionListener(this);
        next.setBounds(105, 235, 100, 30);
//build enter confirm pass text
        verify_code_text.setText("Enter verify code");
        verify_code_text.setBounds(25, 100, 250, 50);
        verify_code_text.setEditable(true);
        verify_code_text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (verify_code_text.getText().equals("Enter verify code")) {
                    verify_code_text.setText("");
                }

            }
        });
        verify_code_text.setVisible(false);
//build the small title of sending email
        verify_label.setBounds(0, 150, 300, 35);
        verify_label.setText("Verify code sent to " + mail);
        verify_label.setForeground(Color.red);
        verify_label.setFont(new Font("Serif", Font.BOLD, 20));
        verify_label.setVisible(false);
//build the verify button
        verify_button.setText("Verify");
        verify_button.setVisible(false);
        verify_button.addActionListener(this);
        verify_button.setBounds(180, 240, 100, 50);
//build the title of register panel
        register_label.setBounds(0, 0, 300, 35);
        register_label.setText("Register");
        register_label.setHorizontalAlignment(0);
        register_label.setVerticalAlignment(0);
        register_label.setForeground(Color.red);
        register_label.setFont(new Font("Serif", Font.BOLD, 30));
        register_label.setVisible(true);
///build register username text label
        register_user_label.setText("Username :");
        register_user_label.setVisible(false);
        register_user_label.setBounds(25, 50, 100, 50);
        register_user_label.setForeground(Color.white);
//build the register username text
        register_username_text.setVisible(false);
        register_username_text.setText("Enter username");
        register_username_text.setBounds(125, 50, 150, 50);
        register_username_text.setEditable(true);
        register_username_text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (register_username_text.getText().equals("Enter username")) {
                    register_username_text.setText("");
                }
            }
        });
        //build register password text
        register_pass_text.setVisible(false);
        register_pass_text.setBounds(125, 150, 150, 50);

        //build register password label
        register_pass_text_label.setText("Password :");
        register_pass_text_label.setVisible(false);
        register_pass_text_label.setBounds(25, 150, 100, 50);
        register_pass_text_label.setForeground(Color.white);
        //build register confirm pass   
        reg_confirm_pass_text.setVisible(false);
        reg_confirm_pass_text.setBounds(125, 255, 150, 50);

        //build register confirm password label  
        reg_confirm_pass_text_label.setText("Re-enter password :");
        reg_confirm_pass_text_label.setVisible(false);
        reg_confirm_pass_text_label.setBounds(15, 255, 120, 50);
        reg_confirm_pass_text_label.setForeground(Color.white);
//build the register button
        register_button.setText("Register");
        register_button.setVisible(false);
        register_button.addActionListener(this);
        register_button.setBounds(105, 350, 100, 30);
//add component to register panel
        register_panel.add(gmail_text);
        register_panel.add(next);
        register_panel.add(verify_label);
        register_panel.add(verify_button);
        register_panel.add(verify_code_text);
        register_panel.add(register_pass_text_label);
        register_panel.add(reg_confirm_pass_text_label);
        register_panel.add(register_pass_text);
        register_panel.add(reg_confirm_pass_text);
        register_panel.add(register_user_label);
        register_panel.add(register_username_text);
        register_panel.add(register_label);
        register_panel.add(register_button);

//build the backgroud panel
        backgroud_panel.setBackground(Color.GREEN);
        backgroud_panel.setOpaque(true);
        backgroud_panel.setLayout(null);
        backgroud_panel.setLocation(0, 0);
        backgroud_panel.setBounds(0, 0, 1330, 690);
        backgroud_panel.setVisible(true);
        backgroud_panel.add(login_panel);
        backgroud_panel.add(register_panel);
//build the main register button
        register.setText("Register");
        register.setVisible(true);
        register.addActionListener(this);
        register.setBounds(500, 200, 300, 100);
//build the main login button
        login.setText("Login");
        login.setVisible(true);
        login.addActionListener(this);
        login.setBounds(500, 400, 300, 100);
//add component to backgroud panel
        backgroud_panel.add(register);
        backgroud_panel.add(login);
        backgroud_panel.add(label);
        //add the component to frame
        frame.repaint();
        frame.add(backgroud_panel);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            register.setVisible(false);
            login.setVisible(false);
            register_panel.setVisible(true);
        }
        if (e.getSource() == login) {
            login.setVisible(false);
            register.setVisible(false);
            login_panel.setVisible(true);
        }
        if (e.getSource() == login_in) {
            String name = login_username.getText();
            String password = new String(login_pass_text.getPassword());
            int b = getindex(name);
            if (b < 0) {
                popwindow("Login Issue", "Invalid username or password. Please try again");
                login_username.setText("Enter username");
                login_pass_text.setText("");
            } else {
                if (people_array.get(b).getPassword().equals(password)) {
                    people_obj = people_array.get(b);
                    can_return = true;
                    login_panel.setVisible(false);
                    frame.setVisible(false);
                } else {
                    popwindow("Password issue", "Invalid username or password. Please try again");
                    login_username.setText("Enter username");
                    login_pass_text.setText("");
                }
            }

        }
        if (e.getSource() == next) {
            mail = gmail_text.getText();
            verify_label.setText("Verify code sent to " + mail);
            send_email(mail); //sending the email;
            gmail_text.setVisible(false);
            verify_code_text.setVisible(true);
            next.setVisible(false);
            verify_label.setVisible(true);
            verify_button.setVisible(true);
        }
        if (e.getSource() == verify_button) {
            num = verify_code_text.getText();
            if (num.equals(verified)) {
                verify_code_text.setVisible(false);
                verify_label.setVisible(false);
                verify_button.setVisible(false);
                register_username_text.setVisible(true);
                register_button.setVisible(true);
                register_user_label.setVisible(true);
                register_pass_text.setVisible(true);
                register_pass_text_label.setVisible(true);
                reg_confirm_pass_text.setVisible(true);
                reg_confirm_pass_text_label.setVisible(true);
            } else {
                verify_code_text.setText("Wrong verified code");
            }

        }
        if (e.getSource() == register_button) {
            String name = register_username_text.getText();
            String password = new String(register_pass_text.getPassword());
            String confirm_pass = new String(reg_confirm_pass_text.getPassword());
            if (people_array.contain(name) == true) {
                popwindow("User Issue", "Name used");
                register_username_text.setText("Enter username");
            } else if (!password_obj.isValid(password)) {
                popwindow("Password Issue", "Weak password");
                register_pass_text.setText("");
                reg_confirm_pass_text.setText("");
            } else if (!password.equals(confirm_pass)) {
                popwindow("Password Issue", "Wrong password");
                register_pass_text.setText("");
                reg_confirm_pass_text.setText("");
            } else {
                register_panel.setVisible(false);
                people_obj = new People(password, name, ++ID, mail);
                people_array.add(people_obj);
                can_return = true;
                frame.setVisible(false);
            }
        }
    }
}
