package com.user.validation.constraints;

import com.user.validation.validators.FileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = FileValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface File {
    String message() default "Invalid file or very large";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] contentType();

    String maxSize();

    boolean nullable() default true;
}
