package com.capgemini.bedwards.almon.notificationcore;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;

public interface Notification {

    void sendNotification(Alert alert);
}
