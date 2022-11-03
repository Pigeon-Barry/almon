package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almondatastore.repository.alert.ScheduledAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIAlertType;
import com.capgemini.bedwards.almon.almonmonitoringapi.repositorty.APIAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.ScheduledAlertServiceImpl;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class APIAlertServiceImpl extends ScheduledAlertServiceImpl<APIAlertType> implements APIAlertService {


    private final APIAlertRepository API_ALERT_REPOSITORY;

    @Autowired
    public APIAlertServiceImpl(NotificationService notificationService, APIAlertRepository apiAlertRepository) {
        super(notificationService);
        this.API_ALERT_REPOSITORY = apiAlertRepository;
    }

    @Override
    protected ScheduledAlertRepository<APIAlertType> getRepository() {
        return API_ALERT_REPOSITORY;
    }
}
