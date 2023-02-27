package com.capgemini.bedwards.almon.almontest.helpers;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ValidatorTest<T extends ConstraintValidator<?, I>, I> {

    protected abstract Stream<Arguments> invalidInputProvider();

    protected abstract Stream<Arguments> validInputProvider();

    protected abstract T getValidator();

    @ParameterizedTest(name = "Invalid: {0}")
    @MethodSource("invalidInputProvider")
    void negative_invalid(I input) throws Exception {
        assertFalse(getValidator().isValid(input, null));
    }

    @ParameterizedTest(name = "Valid: {0}")
    @MethodSource("validInputProvider")
    void positive_valid(I input) throws Exception {
        assertTrue(getValidator().isValid(input, null));
    }

}
