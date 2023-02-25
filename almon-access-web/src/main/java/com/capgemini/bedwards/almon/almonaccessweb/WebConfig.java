package com.capgemini.bedwards.almon.almonaccessweb;

import com.capgemini.bedwards.almon.almonaccessweb.interceptor.NotificationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final NotificationInterceptor NOTIFICATION_INTERCEPTOR;

    @Autowired
    public WebConfig(NotificationInterceptor notificationService) {
        this.NOTIFICATION_INTERCEPTOR = notificationService;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(this.NOTIFICATION_INTERCEPTOR)
                .addPathPatterns("/web/**");
    }
}
