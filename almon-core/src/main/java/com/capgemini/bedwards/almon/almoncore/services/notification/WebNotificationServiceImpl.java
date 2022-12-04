package com.capgemini.bedwards.almon.almoncore.services.notification;

import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import com.capgemini.bedwards.almon.almondatastore.repository.notification.WebNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebNotificationServiceImpl implements WebNotificationService {


  private final WebNotificationRepository WEB_NOTIFICATION_REPOSITORY;

  @Autowired
  public WebNotificationServiceImpl(WebNotificationRepository userNotificationRepository) {
    this.WEB_NOTIFICATION_REPOSITORY = userNotificationRepository;
  }

  @Override
  public void save(WebNotification notification) {
    WEB_NOTIFICATION_REPOSITORY.save(notification);
  }
}
