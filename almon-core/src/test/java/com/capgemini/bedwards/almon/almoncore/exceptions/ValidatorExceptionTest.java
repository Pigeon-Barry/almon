package com.capgemini.bedwards.almon.almoncore.exceptions;

import com.capgemini.bedwards.almon.almontest.helpers.ExceptionTest;
import org.junit.jupiter.api.DisplayName;


@DisplayName("Given I am testing the ValidatorException")
public class ValidatorExceptionTest extends ExceptionTest<ValidatorException> {

    @Override
    public ValidatorException createExceptionWithMessage(String message) {
        return new ValidatorException(message);
    }
}
