package com.boilerplate.ws.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class OverriddenMessage {

    @Autowired
    private MessageSource messageSource;

    public String getMessageFromLocale(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
