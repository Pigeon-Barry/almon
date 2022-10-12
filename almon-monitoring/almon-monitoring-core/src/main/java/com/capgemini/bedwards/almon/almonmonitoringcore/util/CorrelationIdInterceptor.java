package com.capgemini.bedwards.almon.almonmonitoringcore.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class CorrelationIdInterceptor implements HandlerInterceptor {

    private final static String CORRELATION_ID_NAME = "CorrelationId";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final UUID correlationId = getOrGenerateCorrelationId(request);
        response.addHeader(CORRELATION_ID_NAME, correlationId.toString());
        MDC.put(CORRELATION_ID_NAME, correlationId.toString());
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(CORRELATION_ID_NAME);
    }

    private UUID getOrGenerateCorrelationId(final HttpServletRequest request) {
        if (request.getHeaders(CORRELATION_ID_NAME).hasMoreElements()) {
            final String correlationId = request.getHeaders(CORRELATION_ID_NAME).nextElement();
            if (correlationId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
                return UUID.fromString(correlationId);
            }
        }
        return UUID.randomUUID();
    }
}
