package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

import com.capgemini.bedwards.almon.almontest.helpers.GetterSetterHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest implements GetterSetterHelper {

  @ParameterizedTest
  @MethodSource("com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorCodeTest#errorCodeTestData")
  public void positive_constructor(ErrorCode errorCode, String expectedDescription) {
    ErrorResponse errorResponse = new ErrorResponse(errorCode);
    assertEquals(errorCode, errorResponse.getCode());
    assertEquals(expectedDescription, errorResponse.getDescription());
  }


}
