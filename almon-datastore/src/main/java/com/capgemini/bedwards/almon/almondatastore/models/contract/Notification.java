package com.capgemini.bedwards.almon.almondatastore.models.contract;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;

public interface Notification {

    void sendNotification(Alert alert);

    String getDisplayName();

    String getHelpText();

    default String getId() {
        return getClass().getSimpleName();
    }

}
