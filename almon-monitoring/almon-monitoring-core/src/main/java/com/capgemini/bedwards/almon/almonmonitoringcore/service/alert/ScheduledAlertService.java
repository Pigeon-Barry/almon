package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;

public interface ScheduledAlertService<T extends ScheduledAlert> extends AlertService<T> {
    T create(T alert);
}
