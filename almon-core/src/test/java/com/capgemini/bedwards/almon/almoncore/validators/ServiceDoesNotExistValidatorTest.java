package com.capgemini.bedwards.almon.almoncore.validators;

import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                ServiceDoesNotExistValidator.class
        }
)
public class ServiceDoesNotExistValidatorTest {

    @MockBean
    private ServiceService serviceService;
    @Autowired
    private ServiceDoesNotExistValidator serviceDoesNotExistValidator;


    @ParameterizedTest
    @ValueSource(strings = {"Key1", "key2", "newKey"})
    public void positive_service_not_found(String key) {
        when(serviceService.checkKeyExists(eq(key))).thenReturn(false);
        assertTrue(serviceDoesNotExistValidator.isValid(key, null));
        verify(serviceService, times(1)).checkKeyExists(eq(key));
    }

    @Test
    public void positive_service_null_key() {
        assertTrue(serviceDoesNotExistValidator.isValid(null, null));
        verify(serviceService, times(0)).checkKeyExists(any());
    }

    @Test
    public void positive_service_0_length_key() {
        assertTrue(serviceDoesNotExistValidator.isValid("", null));
        verify(serviceService, times(0)).checkKeyExists(any());

    }

    @ParameterizedTest
    @ValueSource(strings = {"Key1", "key2", "newKey"})
    public void negative_service_found(String key) {
        when(serviceService.checkKeyExists(eq(key))).thenReturn(true);
        assertFalse(serviceDoesNotExistValidator.isValid(key, null));
        verify(serviceService, times(1)).checkKeyExists(eq(key));

    }
}