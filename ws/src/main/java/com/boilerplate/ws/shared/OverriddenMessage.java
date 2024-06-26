package com.boilerplate.ws.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class OverriddenMessage {

    @Autowired
    private MessageSource messageSource;

    public String getMessageFromLocale(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public String getMessageFromLocale(String key, Object... args) {
        String message = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        return MessageFormat.format(message, args);
    }
}
