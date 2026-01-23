package com.task.taskreminder.service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import com.task.taskreminder.model.Task;

import java.util.Properties;

@Service
public class EmailService {

    private Session session;

    @PostConstruct
    public void init() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    "sreekumarmuppidi@gmail.com",
                    "ddkjoykjngdfqkdb"
                );
            }
        });
    }

    // ---------------- OTP ----------------
    public void sendOTP(String toEmail, String otp) {
        sendMail(toEmail, "Email Verification",
                "Your OTP is: " + otp);
    }

    // ---------------- TASK CREATED ----------------
    public void sendTaskCreatedMail(String toEmail, Task task) {
        sendMail(
            toEmail,
            "New Task Added",
            "Task: " + task.getTitle() +
            "\nDue: " + task.getDate()
        );
    }

    // ---------------- REMINDER ----------------
    public void sendTaskReminder(String toEmail, Task task) {
        sendMail(
            toEmail,
            "Task Reminder",
            "Your task is due soon:\n" + task.getTitle()
        );
    }

    // ---------------- COMMON METHOD ----------------
    private void sendMail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sreekumarmuppidi@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
