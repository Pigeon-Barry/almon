package com.capgemini.bedwards.almon.almoncore.services.notification;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import java.util.List;

public interface WebNotificationService {

  void save(WebNotification build);

  List<WebNotification> getNotifications(User user);
}
