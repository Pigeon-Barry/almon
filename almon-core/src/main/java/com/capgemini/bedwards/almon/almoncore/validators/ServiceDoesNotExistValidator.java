package com.capgemini.bedwards.almon.almoncore.validators;

import com.capgemini.bedwards.almon.almoncore.service.AuthService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class ServiceDoesNotExistValidator implements ConstraintValidator<ServiceDoesNotExist, String> {


    @Autowired
    ServiceService serviceService;

    @Override
    public void initialize(ServiceDoesNotExist constraintAnnotation) {

    }

    @Override
    public boolean isValid(String key, ConstraintValidatorContext context) {
        return !serviceService.checkKeyExists(key);
    }
}