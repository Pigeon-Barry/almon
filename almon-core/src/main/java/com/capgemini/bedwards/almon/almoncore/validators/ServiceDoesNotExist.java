package com.capgemini.bedwards.almon.almoncore.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceDoesNotExistValidator.class)
@Documented
public @interface ServiceDoesNotExist {
    String message() default "Service does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
