package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almoncore.services.APIKeyService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiKeyAuthenticationFilterTest {

  @Mock
  private APIKeyService apiKeyService;
  @Mock
  private MockFilterChain chain;

  @BeforeEach
  public void before() {
    SecurityContextHolder.clearContext();
  }

  @Test
  public void positive_doFilter_validEnabledKey() throws ServletException, IOException {
    final String apiKey = "myAPIKey";
    final APIKey expectedKey = APIKey.builder()
            .apiKey(apiKey)
            .enabled(true)
            .build();
    when(apiKeyService.getAPIKey(eq(apiKey))).thenReturn(expectedKey);

    MockHttpServletResponse response = doFilter(apiKey, true);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(authentication);
    assertInstanceOf(ApiKeyAuthenticationToken.class, authentication);
    assertEquals(expectedKey, authentication.getCredentials());
    assertEquals(expectedKey, authentication.getPrincipal());
    assertTrue(authentication.isAuthenticated());
    assertEquals("", response.getContentAsString());
  }

  @Test
  public void negative_doFilter_validDisabledKey() throws ServletException, IOException {
    final String apiKey = "myAPIKey";
    final APIKey expectedKey = APIKey.builder()
            .apiKey(apiKey)
            .enabled(false)
            .build();
    when(apiKeyService.getAPIKey(eq(apiKey))).thenReturn(expectedKey);

    MockHttpServletResponse response = doFilter(apiKey);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertNull(authentication);
    assertEquals(401, response.getStatus());
    assertEquals("API Key is disabled", response.getContentAsString());
  }

  @Test
  public void negative_doFilter_invalidKey() throws ServletException, IOException {
    final String apiKey = "invalidAPIKEY";
    when(apiKeyService.getAPIKey(eq(apiKey))).thenReturn(null);
    MockHttpServletResponse response = doFilter(apiKey);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertNull(authentication);
    assertEquals(401, response.getStatus());
    assertEquals("Invalid API Key", response.getContentAsString());
  }


  @Test
  public void positive_doFilter_noKeyProvided() throws ServletException, IOException {
    MockHttpServletResponse response = doFilter(null, true);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertNull(authentication);
    assertEquals(200, response.getStatus());
    assertEquals("", response.getContentAsString());
    //Should continue processing security filter chain
  }


  private MockHttpServletResponse doFilter(String apiKey) throws ServletException, IOException {
    return doFilter(apiKey, false);
  }

  private MockHttpServletResponse doFilter(String apiKey, boolean shouldChainBeCalled)
          throws ServletException, IOException {
    ApiKeyAuthenticationFilter apiKeyAuthenticationFilter = new ApiKeyAuthenticationFilter(
            apiKeyService);

    MockHttpServletRequest req = new MockHttpServletRequest();
    if (apiKey != null) {
      req.addHeader("x-api-key", apiKey);
    }
    MockHttpServletResponse res = new MockHttpServletResponse();
    apiKeyAuthenticationFilter.doFilter(req, res, chain);
    verify(chain, times(shouldChainBeCalled ? 1 : 0)).doFilter(req, res);

    return res;
  }

}
