package com.capgemini.bedwards.almon.almoncore.intergrations.web;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebControllerTest {

  //TODO getUser

  @Test
  public void positive_accessDeniedException() {
    validateExceptionHandler(new TestWebController().accessDeniedException(
                    new AccessDeniedException("Error Message of Exception")),
            "error/error-unauthorized");
  }

  //TODO handleValidationExceptions
  //TODO handleObjectError


  @Test
  public void positive_notFoundException() {
    validateExceptionHandler(new TestWebController().notFoundException(
                    new NotFoundException("Error Message of Exception")),
            "error/error-404");
  }

  @Test
  public void positive_methodArgumentTypeMismatchException() {
    validateExceptionHandler(new TestWebController().methodArgumentTypeMismatchException(
                    new MethodArgumentTypeMismatchException(null, null, null, null, null)),
            "error/error-404");
  }


  @Test
  public void positive_throwable() {
    validateExceptionHandler(new TestWebController().throwable(
                    new Throwable("Error Message of Exception")),
            "error/error-500");
  }

  private void validateExceptionHandler(ModelAndView actualModelAndView, String expectedViewName) {
    assertEquals(expectedViewName, actualModelAndView.getViewName());
  }


  private class TestWebController extends WebController {

  }
}
