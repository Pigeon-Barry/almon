package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorCodeTest {

  public static Stream<Arguments> errorCodeTestData() {
    return Stream.of(
        Arguments.of(NOT_FOUND, "Not Found"),
        Arguments.of(NOT_SUBSCRIBED, "You are not subscribed"),
        Arguments.of(ALREADY_SUBSCRIBED, "You are already subscribed"),
        Arguments.of(INTERNAL_SERVER_ERROR, "Please contact system administrators"),
        Arguments.of(UNAUTHORISED_WEB, "You are not authorised to view this page"),
        Arguments.of(UNAUTHORISED_API, "You are not authorised")
    );
  }


  @ParameterizedTest
  @MethodSource("errorCodeTestData")
  public void positive_enum_creation_test(ErrorCode errorCode, String expectedDescription) {
    assertEquals(expectedDescription, errorCode.getErrorDescription());
  }
}
