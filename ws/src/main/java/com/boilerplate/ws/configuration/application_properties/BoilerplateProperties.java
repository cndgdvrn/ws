package com.boilerplate.ws.configuration.application_properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoilerplateProperties {

    @Autowired
    private EmailProperties emailProperties;

    private StorageProperties storageProperties = new StorageProperties("uploads","profile");




    public EmailProperties getEmailProperties() {
        return emailProperties;
    }

    public void setEmailProperties(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    public StorageProperties getStorageProperties() {
        return storageProperties;
    }

    public void setStorageProperties(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }
}
