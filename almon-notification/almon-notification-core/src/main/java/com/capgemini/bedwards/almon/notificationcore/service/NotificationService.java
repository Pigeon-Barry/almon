package com.capgemini.bedwards.almon.notificationcore.service;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.notificationcore.NotificationHelper;

public interface NotificationService {
    boolean unsubscribe(User user, Service service, Notification notification);

    boolean subscribe(User user, Service service, Notification notification);

    boolean unsubscribe(User user, Monitor monitor, Notification notification);

    boolean subscribe(User user, Monitor monitor, Notification notification);

    NotificationHelper getNotificationHelper();

    <T extends Alert> void send(T alert);

    Notification getNotificationFromId(String source);
}
