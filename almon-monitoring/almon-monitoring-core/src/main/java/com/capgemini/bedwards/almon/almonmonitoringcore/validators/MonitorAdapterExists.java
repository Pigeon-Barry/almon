package com.capgemini.bedwards.almon.almonmonitoringcore.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MonitorAdapterExistsValidator.class)
@Documented
public @interface MonitorAdapterExists {
    String message() default "Monitor Adapter does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
