package com.capgemini.bedwards.almon.almoncore.intergrations.api;

import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidPermissionException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorCode;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class APIControllerTest {


  @Test
  public void positive_resourceNotFoundException() {
    validateExceptionHandler(new TestAPIController().resourceNotFoundException(
                    new NotFoundException("Error Message of Exception")),
            ErrorCode.NOT_FOUND, "Error Message of Exception");
  }

  @Test
  public void positive_accessDeniedException() {
    validateExceptionHandler(new TestAPIController().accessDeniedException(
                    new AccessDeniedException("Error Message of Exception")),
            ErrorCode.UNAUTHORISED_API);
  }

  @Test
  public void positive_authenticationCredentialsNotFoundException() {
    validateExceptionHandler(new TestAPIController().authenticationCredentialsNotFoundException(
                    new AuthenticationCredentialsNotFoundException("Error Message of Exception")),
            ErrorCode.UNAUTHORISED_API);
  }


  @Test
  public void positive_httpRequestMethodNotSupportedException() {
    validateExceptionHandler(new TestAPIController().httpRequestMethodNotSupportedException(
                    new HttpRequestMethodNotSupportedException("Error Message of Exception")),
            ErrorCode.NOT_FOUND, "A request can not be found for this method at this url");
  }

  @Test
  public void positive_invalidPermissionException() {
    validateExceptionHandler(new TestAPIController().invalidPermissionException(
                    new InvalidPermissionException("Error Message of Exception")),
            ErrorCode.UNAUTHORISED_API, "Error Message of Exception");
  }
  //TODO handleValidationExceptions
  //TODO handleObjectError
  //TODO handleValidationExceptions


  @Test
  public void positive_internalServerError() {
    validateExceptionHandler(new TestAPIController().internalServerError(
                    new RuntimeException("Error Message of Exception")),
            ErrorCode.INTERNAL_SERVER_ERROR);
  }

  private void validateExceptionHandler(ErrorResponse actualErrorResponse, ErrorCode errorCode) {
    validateExceptionHandler(actualErrorResponse, errorCode, errorCode.getErrorDescription());
  }

  private void validateExceptionHandler(ErrorResponse actualErrorResponse, ErrorCode errorCode,
                                        String description) {
    assertEquals(actualErrorResponse.getCode(), errorCode);
    assertEquals(actualErrorResponse.getDescription(), description);
  }


  private class TestAPIController extends APIController {

  }

}
