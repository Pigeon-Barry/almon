package com.capgemini.bedwards.almon.almontest.helpers;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface GetterSetterHelper {

  default <T> void getterSetterTest(Consumer<T> setter, Supplier<T> getter, T value) {
    setter.accept(value);
    assertEquals(value, getter.get());
  }


}
