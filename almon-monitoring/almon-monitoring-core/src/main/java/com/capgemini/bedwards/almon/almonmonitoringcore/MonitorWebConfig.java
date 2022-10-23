package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.CreateMonitorRequestResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MonitorWebConfig implements WebMvcConfigurer {

    private final Monitors MONITORS;

    public MonitorWebConfig(Monitors monitors){
        this.MONITORS = monitors;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CreateMonitorRequestResolver(MONITORS));
    }
}