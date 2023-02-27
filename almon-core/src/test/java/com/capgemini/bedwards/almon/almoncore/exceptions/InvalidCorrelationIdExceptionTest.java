package com.capgemini.bedwards.almon.almoncore.exceptions;

import com.capgemini.bedwards.almon.almontest.helpers.ExceptionTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Given I am testing the InvalidCorrelationIdException")
public class InvalidCorrelationIdExceptionTest extends ExceptionTest<InvalidCorrelationIdException> {

    @Override
    public InvalidCorrelationIdException createExceptionWithMessage(String message) {
        return new InvalidCorrelationIdException(message);
    }
}
