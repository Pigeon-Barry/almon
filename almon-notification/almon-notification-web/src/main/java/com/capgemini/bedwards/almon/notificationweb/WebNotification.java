package com.capgemini.bedwards.almon.notificationweb;

import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Slf4j
@Component
public class WebNotification implements Notification {

  private final WebNotificationService WEB_NOTIFICATION_SERVICE;

  @Autowired
  public WebNotification(WebNotificationService webNotificationService) {
    this.WEB_NOTIFICATION_SERVICE = webNotificationService;
  }

  @Override
  public void sendNotification(Set<User> subscribedUsers, Alert<?> alert) {

    com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification notification = com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification.builder()
            .title(alert.getStatus() + ": " + alert.getMonitor().getId().toString() + " - " + alert.getMessage())
            .message(alert.getShortMessage())
        .sentTO(new HashMap<>())
        .build();

    subscribedUsers.forEach(user -> notification.getSentTO().put(user, false));
    WEB_NOTIFICATION_SERVICE.save(notification);
  }

  @Override
  public String getDisplayName() {
    return "Web Notification";
  }

  @Override
  public String getHelpText() {
    return "Upon a failed check. A web notification will be sent.";
  }

  @Override
  public String getId() {
    return "WEB_NOTIFICATION";
  }
}
