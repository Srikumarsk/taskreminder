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

    private final String FROM_EMAIL = "sreekumarmuppidi@gmail.com";
    private final String APP_PASSWORD = "ddkjoykjngdfqkdb"; // ⚠️ Use App Password only

    @PostConstruct
    public void init() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        System.out.println("✅ Email Session Initialized");
    }

    // ---------------- OTP ----------------
    public void sendOTP(String toEmail, String otp) {
        sendMail(
                toEmail,
                "Email Verification - TaskReminder",
                "Your OTP is: " + otp
        );
    }

    // ---------------- TASK CREATED ----------------
    public void sendTaskCreatedMail(String toEmail, Task task) {
        sendMail(
                toEmail,
                "New Task Added",
                "Task: " + task.getTitle() +
                "\nDue Date: " + task.getDate() +
                "\nStatus: " + task.getStatus()
        );
    }

    // ---------------- REMINDER ----------------
    public void sendTaskReminder(String toEmail, Task task) {
        sendMail(
                toEmail,
                "⏰ Task Reminder",
                "Reminder for your task:\n\n" +
                "Title: " + task.getTitle() +
                "\nDue Date: " + task.getDate() +
                "\nStatus: " + task.getStatus()
        );
    }

    // ---------------- COMMON MAIL METHOD ----------------
    public void sendMail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );

            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("📩 Email sent successfully to: " + to);

        } catch (Exception e) {
            System.out.println("❌ Email sending failed");
            e.printStackTrace();
        }
    }

    
}