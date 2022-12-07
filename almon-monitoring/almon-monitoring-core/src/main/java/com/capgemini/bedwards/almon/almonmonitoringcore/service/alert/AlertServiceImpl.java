package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.AlertRepository;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertServiceImpl extends AlertServiceBase<Alert<?>> {

    private final AlertRepository<Alert<?>> ALERT_REPOSITORY;

    @Autowired
    public AlertServiceImpl(NotificationService notificationService,
        AlertRepository<Alert<?>> alertRepository) {
        super(notificationService);
        this.ALERT_REPOSITORY = alertRepository;
    }

    @Override
    protected AlertRepository<Alert<?>> getRepository() {
        return ALERT_REPOSITORY;
    }

}
