package com.capgemini.bedwards.almon.almoncore.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class CronExpressionValidator implements ConstraintValidator<CronExpression, String> {

    @Override
    public void initialize(CronExpression constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return org.springframework.scheduling.support.CronExpression.isValidExpression(value);
    }
}
