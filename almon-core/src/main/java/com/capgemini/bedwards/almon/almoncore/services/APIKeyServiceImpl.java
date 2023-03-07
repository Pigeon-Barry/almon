package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.APIKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class APIKeyServiceImpl implements APIKeyService {

    private final APIKeyRepository API_KEY_REPOSITORY;

    public APIKeyServiceImpl(APIKeyRepository apiKeyRepository) {
        this.API_KEY_REPOSITORY = apiKeyRepository;
    }

    @Override
    public APIKey getAPIKey(String apiKey) {
        Optional<APIKey> apiKeyOptional = this.API_KEY_REPOSITORY.findById(apiKey.trim());
        return apiKeyOptional.orElse(null);
    }
}