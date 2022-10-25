package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.AlertRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AlertServiceImpl<T extends Alert> implements AlertService<T> {


    public AlertServiceImpl() {
    }


    protected abstract AlertRepository<T> getRepository();

    @Override
    public T create(T alert) {
        log.info("Saving alert: " + alert);
       alert = getRepository().save(alert);
       //TODO Trigger send alert to user
       return alert;
    }
}
