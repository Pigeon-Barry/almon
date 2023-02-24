package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.service;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.subscription.SubscriptionService;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorServiceBase;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIAlert;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.TriggerPassiveAPIMonitorRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.repository.PassiveAPIMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PassiveAPIMonitorServiceImpl extends MonitorServiceBase<PassiveAPIMonitor> implements PassiveAPIMonitorService {

    private final PassiveAPIMonitorRepository API_MONITORING_TYPE_REPOSITORY;
    private final PassiveAPIAlertService API_ALERT_SERVICE;

    @Autowired
    public PassiveAPIMonitorServiceImpl(
            PassiveAPIAlertService passiveApiAlertService,
            PassiveAPIMonitorRepository passiveApiMonitorRepository,
            AuthorityService authorityService,
            ServiceService serviceService,
            SubscriptionService subscriptionService) {
        super(authorityService, serviceService, subscriptionService);
        this.API_ALERT_SERVICE = passiveApiAlertService;
        this.API_MONITORING_TYPE_REPOSITORY = passiveApiMonitorRepository;
    }


    @Override
    public List<PassiveAPIMonitor> findAll() {
        return API_MONITORING_TYPE_REPOSITORY.findAll();
    }

    @Override
    public PassiveAPIAlert createAlert(TriggerPassiveAPIMonitorRequestBody body) {
        return null;
    }

    @Override
    protected PassiveAPIMonitorRepository getRepository() {
        return API_MONITORING_TYPE_REPOSITORY;
    }

}
