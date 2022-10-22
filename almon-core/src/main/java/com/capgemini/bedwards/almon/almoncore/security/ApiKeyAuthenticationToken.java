package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final APIKey API_KEY;

    public ApiKeyAuthenticationToken(APIKey apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.API_KEY = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return API_KEY;
    }
}