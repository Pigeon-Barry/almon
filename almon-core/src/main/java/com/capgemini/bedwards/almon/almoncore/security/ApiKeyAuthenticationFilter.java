package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almoncore.services.APIKeyService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class ApiKeyAuthenticationFilter implements Filter {


  private final APIKeyService API_KEY_SERVICE;

  public ApiKeyAuthenticationFilter(APIKeyService apiKeyService) {
    this.API_KEY_SERVICE = apiKeyService;
  }


  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
      String userApiKey = getApiKeyFromRequest((HttpServletRequest) request);
      if (userApiKey != null) {
        log.info("userApiKey: '" + userApiKey + "'");
        APIKey apiKey = this.API_KEY_SERVICE.getAPIKey(userApiKey);
        if (apiKey != null) {
          log.info("API Key: " + apiKey);
          if (apiKey.isEnabled()) {
            ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey);
            SecurityContextHolder.getContext().setAuthentication(apiToken);
          } else {
            writeError(response, "API Key is disabled");
            return;
          }
        } else {
          writeError(response, "Invalid API Key");
          return;
        }
      }
    }
    chain.doFilter(request, response);
  }

  private void writeError(ServletResponse response, String message) throws IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setStatus(401);
    httpResponse.getWriter().write(message);
  }

  private String getApiKeyFromRequest(HttpServletRequest httpRequest) {
    String apiKey = null;
    String authHeader = httpRequest.getHeader(Constants.API_KEY_HEADER);
    if (authHeader != null) {
      apiKey = authHeader.trim();
    }
    return apiKey;
  }
}

