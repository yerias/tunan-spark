package com.tunan.spark.utils.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MsgUtils {
    public static void send(String recivers, String title, String content) throws Exception {

        Properties properties = new Properties();
        properties.setProperty("mail.host","smtp.qq.com");
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable","false");

        MailSSLSocketFactory factory = new MailSSLSocketFactory();
        factory.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.socketFactory", factory);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String username = "971118017@qq.com";
                String password = "tyrmcebuiaskbcej";
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(properties, authenticator);

        MimeMessage message = new MimeMessage(session);
        InternetAddress from = new InternetAddress("971118017@qq.com");
        message.setFrom(from);
        InternetAddress[] tos = InternetAddress.parse(recivers);
        message.setRecipients(Message.RecipientType.TO, tos);
        message.setSubject(title);
        message.setContent(content, "text/html;charset=UTF-8");

        Transport.send(message);
    }

    public static void main(String[] args) throws Exception{
        send("接收邮箱", "测试", "测试内容");
    }
}
