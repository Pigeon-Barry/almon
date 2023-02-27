package com.capgemini.bedwards.almon.almoncore.validators;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                UserDoesNotExistValidator.class
        }
)
public class UserDoesNotExistValidatorTest {

    @MockBean
    private AuthService authService;
    @Autowired
    private UserDoesNotExistValidator validator;

    @ParameterizedTest
    @ValueSource(strings = {"Key1", "key2", "newKey"})
    public void positive_user_not_found(String key) {
        when(authService.checkUserExists(eq(key))).thenReturn(false);
        assertTrue(validator.isValid(key, null));
        verify(authService, times(1)).checkUserExists(eq(key));
    }

    @Test
    public void positive_user_null_key() {
        assertTrue(validator.isValid(null, null));
        verify(authService, times(0)).checkUserExists(any());
    }

    @Test
    public void positive_user_0_length_key() {
        assertTrue(validator.isValid("", null));
        verify(authService, times(0)).checkUserExists(any());

    }

    @ParameterizedTest
    @ValueSource(strings = {"Key1", "key2", "newKey"})
    public void negative_user_found(String key) {
        when(authService.checkUserExists(eq(key))).thenReturn(true);
        assertFalse(validator.isValid(key, null));
        verify(authService, times(1)).checkUserExists(eq(key));

    }
}
