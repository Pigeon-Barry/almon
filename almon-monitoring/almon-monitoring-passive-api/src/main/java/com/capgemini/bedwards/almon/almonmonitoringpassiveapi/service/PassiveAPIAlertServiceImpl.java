package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.service;

import com.capgemini.bedwards.almon.almoncore.services.notification.NotificationService;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertServiceBase;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIAlert;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.repository.PassiveAPIAlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class PassiveAPIAlertServiceImpl extends AlertServiceBase<PassiveAPIAlert> implements
    PassiveAPIAlertService {


    private final PassiveAPIAlertRepository API_ALERT_REPOSITORY;

    @Autowired
    public PassiveAPIAlertServiceImpl(NotificationService notificationService,
        PassiveAPIAlertRepository apiAlertRepository) {
        super(notificationService);
        this.API_ALERT_REPOSITORY = apiAlertRepository;
    }

    @Override
    protected PassiveAPIAlertRepository getRepository() {
        return API_ALERT_REPOSITORY;
    }
}
