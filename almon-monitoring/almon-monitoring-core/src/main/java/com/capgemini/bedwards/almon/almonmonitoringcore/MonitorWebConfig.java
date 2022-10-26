package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.MonitorConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.CreateMonitorRequestResolver;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MonitorWebConfig implements WebMvcConfigurer {

    private final Monitors MONITORS;
    public final MonitorService<MonitoringType> MONITOR_SERVICE;

    @Autowired

    public MonitorWebConfig(Monitors monitors, @Qualifier("monitorServiceImpl")  MonitorService<MonitoringType> monitorService) {
        this.MONITORS = monitors;
        this.MONITOR_SERVICE = monitorService;

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CreateMonitorRequestResolver(MONITORS));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MonitorConvertor(MONITOR_SERVICE));
    }
}