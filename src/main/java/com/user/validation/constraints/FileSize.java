package com.user.validation.constraints;

import com.user.validation.validators.FileSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = FileSizeValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSize {

    int KB = 1024;
    int MB = 1024 * 1024;

    String message() default "File size mus be less then {value} * {measurement} bytes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();

    int measurement() default MB;
}
