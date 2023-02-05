package com.capgemini.bedwards.almon.almondatastore.models.contract;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;

import java.util.Set;

public interface Notification {

  void sendNotification(Set<User> subscribedUsers, Alert<?> alert);

  String getDisplayName();

  String getHelpText();

  default boolean isEnabled() {
    return true;
  }

  default String getId() {
    return getClass().getSimpleName();
  }

  default boolean requiresConfig() {
    return false;
  }

  default NotificationType getNotificationType() {
    return NotificationType.USER;
  }
}
