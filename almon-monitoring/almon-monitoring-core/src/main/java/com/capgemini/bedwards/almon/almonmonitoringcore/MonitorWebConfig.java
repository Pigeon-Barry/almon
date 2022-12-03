package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.AlertConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.MonitorAdapterConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.MonitorConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.NotificationConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.ScheduledMonitorConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.ServiceConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.UserIdConvertor;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.CreateMonitorRequestResolver;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.UpdateMonitorRequestResolver;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertService;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorService;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MonitorWebConfig implements WebMvcConfigurer {

  private final Monitors MONITORS;
  public final MonitorService<Monitor> MONITOR_SERVICE;
  public final ScheduledMonitorService<ScheduledMonitor> SCHEDULED_MONITOR_SERVICE;
  public final AlertService<Alert<?>> ALERT_SERVICE;
  public final ServiceService SERVICE_SERVICE;
  public final NotificationService NOTIFICATION_SERVICE;
  public final UserService USER_SERVICE;

  @Autowired

  public MonitorWebConfig(Monitors monitors,
      @Qualifier("monitorServiceImpl") MonitorService<Monitor> monitorService,
      @Qualifier("scheduledMonitorServiceImpl") ScheduledMonitorService<ScheduledMonitor> scheduledMonitorService,
      @Qualifier("alertServiceImpl") AlertService<Alert<?>> alertService,
      ServiceService serviceService,
      UserService userService,
      NotificationService notificationService) {
    this.MONITORS = monitors;
    this.USER_SERVICE = userService;
    this.SERVICE_SERVICE = serviceService;
    this.MONITOR_SERVICE = monitorService;
    this.SCHEDULED_MONITOR_SERVICE = scheduledMonitorService;
    this.ALERT_SERVICE = alertService;
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
    registry.addConverter(new UserIdConvertor(USER_SERVICE));
    registry.addConverter(new ScheduledMonitorConvertor(SCHEDULED_MONITOR_SERVICE));
    registry.addConverter(new MonitorAdapterConvertor(MONITORS));
    registry.addConverter(new ServiceConvertor(SERVICE_SERVICE));
    registry.addConverter(new NotificationConvertor(NOTIFICATION_SERVICE));
    registry.addConverter(new AlertConvertor(ALERT_SERVICE));
  }
}