package com.user.validation.validators;


import com.user.validation.constraints.File;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.asList;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {

    private String[] contentType;
    private String maxSize;
    private Boolean nullable;

    @Override
    public void initialize(File constraintAnnotation) {
        contentType = constraintAnnotation.contentType();
        maxSize = constraintAnnotation.maxSize();
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return nullable && value.isEmpty() ||
                asList(contentType).contains(value.getContentType().toLowerCase()) &&
                        value.getSize() < parseSize(maxSize);
    }

    private long parseSize(String size) {
        Assert.hasLength(size, "Size must not be empty");
        size = size.toUpperCase();
        if (size.endsWith("KB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024;
        }
        if (size.endsWith("MB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024 * 1024;
        }
        return Long.valueOf(size);
    }
}
