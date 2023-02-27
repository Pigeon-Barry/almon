package com.capgemini.bedwards.almon.almoncore.validators;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class UserDoesNotExistValidator implements ConstraintValidator<UserDoesNotExist, String> {



    private final AuthService AUTH_SERVICE;

    @Autowired
    public UserDoesNotExistValidator(AuthService authService){
        this.AUTH_SERVICE = authService;
    }

    @Override
    public void initialize(UserDoesNotExist constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0)
            return true;
        return !AUTH_SERVICE.checkUserExists(value);
    }
}