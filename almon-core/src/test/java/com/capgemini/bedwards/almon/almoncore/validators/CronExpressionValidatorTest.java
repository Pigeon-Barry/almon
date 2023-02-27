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
                CronExpressionValidator.class
        }
)
public class CronExpressionValidatorTest extends ValidatorTest<CronExpressionValidator, String> {

    @Autowired
    private CronExpressionValidator cronExpressionValidator;


    @Override
    protected Stream<Arguments> invalidInputProvider() {
        return Stream.of(
                arguments((String) null),
                arguments(""),
                arguments("* * * * *"),
                arguments("G * * * * *"),
                arguments("INVALID"),
                arguments("991S * * * * *")
        );
    }

    @Override
    protected Stream<Arguments> validInputProvider() {
        return Stream.of(
                arguments("@yearly"),
                arguments("@annually"),
                arguments("@monthly"),
                arguments("@weekly"),
                arguments("@daily"),
                arguments("@midnight"),
                arguments("0 0 * * * *"),
                arguments("*/10 * * * * *"),
                arguments("0 0 8-10 * * *"),
                arguments("0 0 6,19 * * *"),
                arguments("0 0/30 8-10 * * *"),
                arguments("0 0 9-17 * * MON-FRI"),
                arguments("0 0 0 25 12 ?"),
                arguments("0 0 0 L * *"),
                arguments("0 0 0 L-3 * *"),
                arguments("0 0 0 1W * *"),
                arguments("0 0 0 LW * *"),
                arguments("0 0 0 * * 5L"),
                arguments("0 0 0 * * THUL"),
                arguments("0 0 0 ? * 5#2"),
                arguments("0 0 0 ? * MON#1")
        );
    }

    @Override
    protected CronExpressionValidator getValidator() {
        return cronExpressionValidator;
    }
}
