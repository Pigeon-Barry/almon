package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;

public interface AlertService<T extends Alert> {
    T create(T monitorType);
}
