package com.boilerplate.ws.user.validation;

import com.boilerplate.ws.file.FileService;
import com.boilerplate.ws.shared.OverriddenMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class FileTypeValidator implements ConstraintValidator<FileType, String> {

    @Autowired
    private FileService fileService;

    @Autowired
    private OverriddenMessage overriddenMessage;

    private String [] allowedTypes;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String detectedFileType = fileService.detectFileType(value);
        for (String allowedType : allowedTypes) {
            if (detectedFileType.contains(allowedType)) {
                return true;
            }
        }

        String messageTemplate=overriddenMessage.getMessageFromLocale("boilerplate.not.allowed.file.type", String.join(", ", allowedTypes));

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        return false;
    }
}



