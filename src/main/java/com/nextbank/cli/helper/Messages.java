package com.nextbank.cli.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Messages {

    private MessageSourceAccessor accessor;

    @Autowired
    public Messages(MessageSource messageSource) {
        this.accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }

    public String get(String code, String... params) {
        return accessor.getMessage(code, params);
    }
}