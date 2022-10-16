package com.capgemini.bedwards.almon.almonwebcore.security;

import com.capgemini.bedwards.almon.almoncore.services.APIKeyService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


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
                APIKey apiKey = this.API_KEY_SERVICE.getAPIKey(userApiKey);
                if (apiKey != null) {
                    log.info("API Key: " + apiKey);
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    if (apiKey.getAuthorities() != null)
                        apiKey.getAuthorities().forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getAuthority())));

                    ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey, authorities);
                    SecurityContextHolder.getContext().setAuthentication(apiToken);
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(401);
                    httpResponse.getWriter().write("Invalid API Key");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
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

