package com.capgemini.bedwards.almon.almoncore.services.notification;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;

import java.util.List;

public interface NotificationService {

    <T extends Alert<?>> void send(T alert);

    Notification getNotificationFromId(String source);

    List<Notification> getNotificationTypes();

}
