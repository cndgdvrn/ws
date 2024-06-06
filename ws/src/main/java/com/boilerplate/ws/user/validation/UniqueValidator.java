package com.boilerplate.ws.user.validation;

import com.boilerplate.ws.shared.OverriddenMessage;
import com.boilerplate.ws.user.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private OverriddenMessage overriddenMessage;

    String fieldName;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        boolean exists = false;
        if ("email".equals(fieldName)) {
            exists = userRepository.existsByEmail(value);
        } else if ("username".equals(fieldName)) {
            exists = userRepository.existsByUsername(value);
        }

        if (exists) {
            String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.NotUnique." + this.fieldName);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageTemplate)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
