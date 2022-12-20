package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InternalServerErrorResponseTest {

  @Test
  public void positive_constructor_test() {
    InternalServerErrorResponse internalServerErrorResponse = new InternalServerErrorResponse();
    assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, internalServerErrorResponse.getCode());
    assertEquals("Please contact system administrators",
            internalServerErrorResponse.getDescription());
  }

}
