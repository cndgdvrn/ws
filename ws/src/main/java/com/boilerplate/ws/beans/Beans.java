package com.boilerplate.ws.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;


@Component
public class Beans {

    @Bean
    public JavaMailSenderImpl getJavaMailSender(){
        return new JavaMailSenderImpl();
    }

    @Bean
    public SimpleMailMessage getSimpleMailMessage(){
        return new SimpleMailMessage();
    }


}
