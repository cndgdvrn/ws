package com.boilerplate.ws.configuration.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tika.Tika;
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

    @Bean
    public Tika getTika(){
        return new Tika();
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }


}
