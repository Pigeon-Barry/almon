package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationIdConvertor implements Converter<UUID, WebNotification> {


  public final WebNotificationService WEB_NOTIFICATION_SERVICE;

  public NotificationIdConvertor(WebNotificationService webNotificationService) {
    this.WEB_NOTIFICATION_SERVICE = webNotificationService;
  }

  @Override
  public WebNotification convert(UUID source) {
    return WEB_NOTIFICATION_SERVICE.findByWebNotificationId(source);
  }
}
