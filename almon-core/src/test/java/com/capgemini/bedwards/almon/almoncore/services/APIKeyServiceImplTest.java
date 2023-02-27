package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.APIKeyRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                APIKeyServiceImpl.class
        }
)
public class APIKeyServiceImplTest {
    @MockBean
    private APIKeyRepository apiKeyRepository;
    @Autowired
    private APIKeyServiceImpl apiKeyService;

    @ParameterizedTest
    @ValueSource(strings = {"key1", "newKey", "key3"})
    public void positive_getAPIKey_typical(String apiKeyStr) {
        APIKey apiKey = APIKey.builder().apiKey(apiKeyStr).build();
        when(apiKeyRepository.findById(eq(apiKeyStr))).thenReturn(Optional.of(apiKey));
        assertEquals(apiKey, apiKeyService.getAPIKey(apiKeyStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {"key1", "newKey", "key3"})
    public void negative_getAPIKey_not_found(String apiKeyStr) {
        when(apiKeyRepository.findById(eq(apiKeyStr))).thenReturn(Optional.empty());
        assertNull(apiKeyService.getAPIKey(apiKeyStr));
    }
}
