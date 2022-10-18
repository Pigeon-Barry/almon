package com.capgemini.bedwards.almon.almonwebcore.exception.handling;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almonwebcore.model.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class WebExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        User user = Util.getAuthenticatedUser();
        if (user != null) {
            if (user.getApprovedBy() == null) {
                response.sendRedirect("/web/auth/pendingApproval");
                return;
            }
            if (!user.isEnabled()) {
                response.sendRedirect("/web/auth/accountDisabled");
                return;
            }
        }
        response.sendRedirect("/web/error");
    }

}

