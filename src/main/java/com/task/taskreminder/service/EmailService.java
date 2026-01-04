package com.task.taskreminder.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    public void sendOTP(String toEmail, String otp) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "sreekumarmuppidi@gmail.com",
                        "ddkjoykjngdfqkdb"
                );
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sreekumarmuppidi@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject("OTP Verification");
            message.setText("Your OTP is: " + otp);

            Transport.send(message);
            System.out.println("OTP SENT to " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
