package com.capgemini.bedwards.almon.almonaccessweb.interceptor;

import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class NotificationInterceptor implements HandlerInterceptor {

    private final WebNotificationService WEB_NOTIFICATION_SERVICE;

    @Autowired
    public NotificationInterceptor(WebNotificationService webNotificationService) {
        this.WEB_NOTIFICATION_SERVICE = webNotificationService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView == null || SecurityUtil.getAuthenticatedUser() == null)
            return;
        modelAndView.getModel().put("unreadNotificationCount", this.WEB_NOTIFICATION_SERVICE.getUnreadNotifications(SecurityUtil.getAuthenticatedUser()));
    }
}
