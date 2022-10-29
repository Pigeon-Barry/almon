package com.capgemini.bedwards.almon.almoncore.validators;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class ValidJsonPathValidator implements ConstraintValidator<ValidJsonPath, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            JsonPath jsonPath = JsonPath.compile(value);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
