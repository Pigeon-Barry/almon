package com.capgemini.bedwards.almon.almontest.data;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class Providers {

  public static Stream<Arguments> basicStringArgumentProvider() {
    return Stream.of(
            Arguments.of("Typical Value"),
            Arguments.of("!"),
            Arguments.of("&*(!\"'#"),
            Arguments.of((String) null),
            Arguments.of(
                    "A very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer strings")
    );
  }

  public static Stream<Arguments> basicDuelStringArgumentProvider() {
    return Stream.of(
            Arguments.of("Typical Value", "Second Typical Value"),
            Arguments.of("!", "A second value is here"),
            Arguments.of("&*(!\"'#", "Oh my we have special characters here!\"£$%^^&*"),
            Arguments.of(null, "Only the first param is null here"),
            Arguments.of("Only the second param is null here", null),
            Arguments.of(null, null),
            Arguments.of(
                    "A very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer stringsA very long value that purpose is to test that the setter can handle longer strings",
                    "this is going to also be a very long string as this is validating that long values are allowedthis is going to also be a very long string as this is validating that long values are allowedthis is going to also be a very long string as this is validating that long values are allowedthis is going to also be a very long string as this is validating that long values are allowedthis is going to also be a very long string as this is validating that long values are allowedthis is going to also be a very long string as this is validating that long values are allowedthis is going to also be a very long string as this is validating that long values are allowedthis is going to also be a very long string as this is validating that long values are allowed")
    );
  }
}
