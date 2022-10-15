package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;

public interface APIKeyService {
    APIKey getAPIKey(String apiKey);
}
