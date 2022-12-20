package com.capgemini.bedwards.almon.almoncore.exceptions;

import com.capgemini.bedwards.almon.almontest.helpers.ExceptionTest;
import org.junit.jupiter.api.DisplayName;


@DisplayName("Given I am testing the NotFoundException")
public class NotFoundExceptionTest extends ExceptionTest<NotFoundException> {

    @Override
    public NotFoundException createExceptionWithMessage(String message) {
        return new NotFoundException(message);
    }
}
