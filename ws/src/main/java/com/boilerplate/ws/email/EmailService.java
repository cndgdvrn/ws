package com.boilerplate.ws.email;

import com.boilerplate.ws.configuration.BoilerplateProperties;
import com.boilerplate.ws.shared.OverriddenMessage;
import com.boilerplate.ws.user.User;
import com.boilerplate.ws.user.exception.ActivationMailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private SimpleMailMessage mailMessage;

    @Autowired
    private BoilerplateProperties boilerplateProperties;

    @Autowired
    OverriddenMessage overriddenMessage;

    public JavaMailSender getJavaMailSender() {

        mailSender.setHost(boilerplateProperties.getEmailProperties().getHost());
        mailSender.setPort(boilerplateProperties.getEmailProperties().getPort());
        mailSender.setUsername(boilerplateProperties.getEmailProperties().getUsername());
        mailSender.setPassword(boilerplateProperties.getEmailProperties().getPassword());
        mailSender.setProtocol(boilerplateProperties.getEmailProperties().getProtocol());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }

    public void sendUserActivationEmail(User user) {

        String activationUrl = boilerplateProperties.getEmailProperties().getClientHost() + "/user/activate?token=" + user.getActivationToken();
        String htmlTemplate =
                "<html>" +
                        "<body style=\"font-family: Arial, sans-serif;\">" +
                        "<h2 style=\"color: #333333;\">{0}</h2>" +
                        "<p>{1} " + user.getUsername() + ",</p>" +
                        "<p>{2}</p>" +
                        "<a href=\"" + activationUrl + "\" style=\"color: #1a73e8; text-decoration: none;\">{3}</a>" +
                        "<p>{4}</p>" +
                        "<p style=\"word-break: break-all;\">" + activationUrl + "</p>" +
                        "<p>{5},<br>Boilerplate </p>" +
                        "</body>" +
                        "</html>";

        String htmlContent = MessageFormat.format(
                htmlTemplate,
                overriddenMessage.getMessageFromLocale("boilerplate.activation.mail.subject"),
                overriddenMessage.getMessageFromLocale("boilerplate.activation.mail.dear"),
                overriddenMessage.getMessageFromLocale("boilerplate.activation.click.link"),
                overriddenMessage.getMessageFromLocale("boilerplate.activation.active.now"),
                overriddenMessage.getMessageFromLocale("boilerplate.activation.copy.link"),
                overriddenMessage.getMessageFromLocale("boilerplate.activation.mail.regards")
        );


        try {
            MimeMessage mimeMessage = getJavaMailSender().createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

            mimeMessageHelper.setFrom(boilerplateProperties.getEmailProperties().getFrom());
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject(overriddenMessage.getMessageFromLocale("boilerplate.activation.mail.subject"));
            mimeMessageHelper.setText(htmlContent,true);
            getJavaMailSender().send(mimeMessage);
        } catch (MessagingException e) {
            throw new ActivationMailException("MessagingException thrown while sending activation email");
        }

    }

}







