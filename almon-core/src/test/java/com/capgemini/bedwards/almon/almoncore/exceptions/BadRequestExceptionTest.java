package com.capgemini.bedwards.almon.almoncore.exceptions;

import com.capgemini.bedwards.almon.almontest.helpers.ExceptionTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Given I am testing the BadRequestException")
public class BadRequestExceptionTest extends ExceptionTest<BadRequestException> {


    @Override
    public BadRequestException createExceptionWithMessage(String message) {
        return new BadRequestException(message);
    }
}
