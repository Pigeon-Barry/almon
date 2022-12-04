package com.capgemini.bedwards.almon.almoncore.services.notification;

import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;

public interface WebNotificationService {

  void save(WebNotification build);
}
