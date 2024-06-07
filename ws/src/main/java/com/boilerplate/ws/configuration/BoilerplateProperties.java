package com.boilerplate.ws.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoilerplateProperties {

    @Autowired
    private EmailProperties emailProperties;

    public EmailProperties getEmailProperties() {
        return emailProperties;
    }

    public void setEmailProperties(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }
}
