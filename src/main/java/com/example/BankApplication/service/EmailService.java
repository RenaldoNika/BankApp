package com.example.BankApplication.service;

import com.example.BankApplication.model.User;
import com.example.BankApplication.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {

       ;


        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("roniii2017@icloud.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(message);
            log.info("Email successfully sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
        }
    }
}
