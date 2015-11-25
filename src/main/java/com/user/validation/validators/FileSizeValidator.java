package com.user.validation.validators;


import com.user.validation.constraints.FileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private int value;
    private int measurement;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        value = constraintAnnotation.value();
        measurement = constraintAnnotation.measurement();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file.getSize() < value * measurement;
    }
}
