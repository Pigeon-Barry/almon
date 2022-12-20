package com.capgemini.bedwards.almon.almoncore.exceptions;

import com.capgemini.bedwards.almon.almontest.helpers.ExceptionTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Given I am testing the InvalidPermissionException")
public class InvalidPermissionExceptionTest extends ExceptionTest<InvalidPermissionException> {

    @Override
    public InvalidPermissionException createExceptionWithMessage(String message) {
        return new InvalidPermissionException(message);
    }
}
