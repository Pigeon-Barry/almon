package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.APIKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class APIKeyServiceImpl implements APIKeyService {

    @Autowired
    APIKeyRepository apiKeyRepository;

    @Override
    public APIKey getAPIKey(String apiKey) {
        Optional<APIKey> apiKeyOptional = apiKeyRepository.findById(apiKey);
        return apiKeyOptional.orElse(null);
    }
}