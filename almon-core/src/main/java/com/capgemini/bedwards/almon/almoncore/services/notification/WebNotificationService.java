package com.capgemini.bedwards.almon.almoncore.services.notification;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface WebNotificationService {

  void save(WebNotification build);

  List<WebNotification> getNotifications(User user);

  Page<WebNotification> getNotifications(User user, int notificationPageNumber,
      int notificationPageSize);

  void read(User authenticatedUser, WebNotification webNotification);

  Optional<WebNotification> findById(UUID id);

  WebNotification findByWebNotificationId(UUID id);
}
