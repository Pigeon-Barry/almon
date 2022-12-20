package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

import com.capgemini.bedwards.almon.almontest.helpers.GetterSetterHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadRequestResponseTest implements GetterSetterHelper {


  @ParameterizedTest
  @MethodSource("com.capgemini.bedwards.almon.almontest.data.Providers#basicDuelStringArgumentProvider")
  public void positive_constructor(String field, String error) {
    BadRequestResponse badRequestResponse = new BadRequestResponse(field, error);
    assertEquals(field, badRequestResponse.getField());
    assertEquals(error, badRequestResponse.getError());
  }

  @ParameterizedTest
  @MethodSource("com.capgemini.bedwards.almon.almontest.data.Providers#basicStringArgumentProvider")
  public void positive_fieldParam_getterSetterTest(String value) {
    BadRequestResponse badRequestResponse = new BadRequestResponse(null, null);
    getterSetterTest(o -> badRequestResponse.setField(value), badRequestResponse::getField, value);
  }

  @ParameterizedTest
  @MethodSource("com.capgemini.bedwards.almon.almontest.data.Providers#basicStringArgumentProvider")
  public void positive_errorParam_getterSetterTest(String value) {
    BadRequestResponse badRequestResponse = new BadRequestResponse(null, null);
    getterSetterTest(o -> badRequestResponse.setError(value), badRequestResponse::getError, value);
  }
}
