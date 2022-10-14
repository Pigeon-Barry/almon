package com.capgemini.bedwards.almon.almonwebcore.security;

import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ApiKeyAuthenticationFilter implements Filter {
    private String tmpValidKey = "SomeKey1234567890"; //TODO replace

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String apiKey = getApiKey((HttpServletRequest) request);
            if (apiKey != null) {
                if (apiKey.equals(tmpValidKey)) {
                    ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
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

    private String getApiKey(HttpServletRequest httpRequest) {
        String apiKey = null;
        String authHeader = httpRequest.getHeader(Constants.API_KEY_HEADER);
        if (authHeader != null) {
            apiKey = authHeader.trim();
        }
        return apiKey;
    }

}

