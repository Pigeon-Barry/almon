package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.MonitorAdapterConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.MonitorConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.NotificationConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.ServiceConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.CreateMonitorRequestResolver;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.UpdateMonitorRequestResolver;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
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
    public final MonitorService<Monitor> MONITOR_SERVICE;
    public final ServiceService SERVICE_SERVICE;
    public final NotificationService NOTIFICATION_SERVICE;

    @Autowired

    public MonitorWebConfig(Monitors monitors,
                            @Qualifier("monitorServiceImpl") MonitorService<Monitor> monitorService,
                            ServiceService serviceService,
                            NotificationService notificationService) {
        this.MONITORS = monitors;
        this.SERVICE_SERVICE = serviceService;
        this.MONITOR_SERVICE = monitorService;
        this.NOTIFICATION_SERVICE = notificationService;

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CreateMonitorRequestResolver(MONITORS));
        argumentResolvers.add(new UpdateMonitorRequestResolver(MONITORS));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MonitorConvertor(MONITOR_SERVICE));
        registry.addConverter(new MonitorAdapterConvertor(MONITORS));
        registry.addConverter(new ServiceConvertor(SERVICE_SERVICE));
        registry.addConverter(new NotificationConvertor(NOTIFICATION_SERVICE));
    }
}