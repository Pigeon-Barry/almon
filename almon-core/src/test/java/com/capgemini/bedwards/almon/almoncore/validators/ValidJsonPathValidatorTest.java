package com.capgemini.bedwards.almon.almoncore.validators;

import com.capgemini.bedwards.almon.almontest.helpers.ValidatorTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                ValidJsonPathValidator.class
        }
)
public class ValidJsonPathValidatorTest extends ValidatorTest<ValidJsonPathValidator, String> {

    @Autowired
    private ValidJsonPathValidator validJsonPathValidator;

    @Override
    protected Stream<Arguments> invalidInputProvider() {
        return Stream.of(
                arguments((String) null),
                arguments(""),
                arguments("$$$"),
                arguments("...")
        );
    }

    @Override
    protected Stream<Arguments> validInputProvider() {
        return Stream.of(
                arguments("$.myValue"),
                arguments("key.subKey.theOneIWant"),
                arguments("$.myValue[0].val")
        );
    }

    @Override
    protected ValidJsonPathValidator getValidator() {
        return validJsonPathValidator;
    }
}
