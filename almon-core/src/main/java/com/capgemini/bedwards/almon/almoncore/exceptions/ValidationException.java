package com.capgemini.bedwards.almon.almoncore.exceptions;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException{


    private final Errors ERRORS;
    public ValidationException(Errors errors){
        super(errors.toString());
        this.ERRORS = errors;
    }

    public Errors getErrors(){
        return this.ERRORS;
    }
}
