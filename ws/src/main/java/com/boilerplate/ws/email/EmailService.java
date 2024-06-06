package com.boilerplate.ws.email;

import com.boilerplate.ws.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private SimpleMailMessage mailMessage;

    public JavaMailSender getJavaMailSender() {
        mailSender.setHost("smtp.ethereal.email");
        mailSender.setPort(587);
        mailSender.setUsername("holly.cassin@ethereal.email");
        mailSender.setPassword("SkYVUmexn3GcsTPeEz");
        mailSender.setProtocol("smtp");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }


    public void sendUserActivationEmail(User user) {
        mailMessage.setFrom("test@test.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Account Activation");
        mailMessage.setText("http://localhost:8080/user/activate?token=" + user.getActivationToken());
        getJavaMailSender().send(mailMessage);
    }

}
