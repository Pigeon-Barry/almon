package com.capgemini.bedwards.almon.almontest.helpers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExceptionTest<E extends Exception> {


  @ParameterizedTest
  @ValueSource(strings = {"My First Message", "My Second Message", "An error has occurred"})
  public void positive_getMessage_canGetSavedMessage(String message) {
    E exception = createExceptionWithMessage(message);
    assertEquals(message, exception.getMessage());
  }

  public abstract E createExceptionWithMessage(String message);

}
