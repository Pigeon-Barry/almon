package com.capgemini.bedwards.almon.almoncore.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserDoesNotExistValidator.class)
@Documented
public @interface UserDoesNotExist {
    String message() default "{com.capgemini.bedwards.almon.almoncore.validators.UserDoesNotExist.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
