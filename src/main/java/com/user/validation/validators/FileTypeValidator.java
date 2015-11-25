package com.user.validation.validators;


import com.user.validation.constraints.FileType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.asList;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

    private String[] contentType;
    private Boolean nullable;

    @Override
    public void initialize(FileType constraintAnnotation) {
        contentType = constraintAnnotation.value();
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return nullable && file.isEmpty() ||
                asList(contentType).contains(file.getContentType().toLowerCase());
    }
}
