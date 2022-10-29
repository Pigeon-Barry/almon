package com.capgemini.bedwards.almon.almoncore.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidJsonPathValidator.class)
@Documented
public @interface ValidJsonPath {
    String message() default "Must be a valid JsonPath";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
