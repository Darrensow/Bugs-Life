package Semag;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * you are required to downloadd this jar file add add it to your project to run
 * linl: https://static.javatpoint.com/src/mail/mailactivation.zip
 *
 * @author xianp
 */
public class gmail_sender {

    private String send_to;
    private String send_by = "xianpua2001.sim02@gmail.com";
    private String send_by_password = "@Xianpua2001.sim02"; //this is  fake password =, change the your owner password and send_by gmail
    private String title;
    private String content;
    private String host = "smtp.gmail.com";

    public gmail_sender(String send_to, String title, String content) {
        this.send_to = send_to;
        this.title = title;
        this.content = content;
    }

    public void sendattachment(File[] file) {
        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(send_by, send_by_password);

            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(send_by));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(send_to));

            // Set Subject: header field
            message.setSubject(title);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart[] attachmentPart = new MimeBodyPart[file.length];

            MimeBodyPart textPart = new MimeBodyPart();

            try {
                for (int i = 0; i < file.length; i++) {
                    attachmentPart[i] = new MimeBodyPart();
                    attachmentPart[i].attachFile(file[i]);
                    multipart.addBodyPart(attachmentPart[i]);
                }
                textPart.setText(content);
                multipart.addBodyPart(textPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            // Send message
            Transport.send(message);
//            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void send() {

        // Assuming you are sending email from through gmails smtp
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(send_by, send_by_password);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(send_by));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(send_to));

            // Set Subject: header field
            message.setSubject(title);

            // Now set the actual message
            message.setText(content);

            // Send message
            Transport.send(message);
//            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
}
