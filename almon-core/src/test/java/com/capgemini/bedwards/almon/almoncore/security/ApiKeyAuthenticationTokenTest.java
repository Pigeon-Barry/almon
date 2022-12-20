package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ApiKeyAuthenticationTokenTest {


  @Test
  public void positive_constructor_validArguments() {
    APIKey apiKey = getAPIKey();
    ApiKeyAuthenticationToken apiKeyAuthenticationToken = new ApiKeyAuthenticationToken(apiKey);

    assertEquals(apiKey, apiKeyAuthenticationToken.getPrincipal());
    assertEquals(apiKey, apiKeyAuthenticationToken.getCredentials());
    assertIterableEquals(apiKey.getAuthorities(), apiKeyAuthenticationToken.getAuthorities());
    assertTrue(apiKeyAuthenticationToken.isAuthenticated());
  }

  private APIKey getAPIKey() {
    return APIKey.builder()
        .apiKey("Test Key")
        .authorities(new HashSet<Authority>() {{
          add(Authority.builder().authority("Test Authority").build());
          add(Authority.builder().authority("Test Authority 2").build());
        }})
        .build();
  }


}
