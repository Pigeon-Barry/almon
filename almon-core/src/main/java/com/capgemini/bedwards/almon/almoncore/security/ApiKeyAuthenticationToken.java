package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final APIKey API_KEY;

    public ApiKeyAuthenticationToken(APIKey apiKey) {
        super(SecurityUtil.getAuthoritiesFromAPiKey(apiKey));
        this.API_KEY = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return API_KEY;
    }

    @Override
    public Object getPrincipal() {
        return API_KEY;
    }
}