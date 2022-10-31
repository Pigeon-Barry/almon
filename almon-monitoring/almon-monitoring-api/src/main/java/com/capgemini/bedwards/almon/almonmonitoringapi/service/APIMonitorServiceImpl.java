package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringapi.repositorty.APIMonitorRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class APIMonitorServiceImpl extends ScheduledMonitorServiceBase<APIMonitor> implements APIMonitorService {

    private final APIMonitorRepository API_MONITORING_TYPE_REPOSITORY;
    private final APIAlertService API_ALERT_SERVICE;

    @Autowired
    public APIMonitorServiceImpl(
            APIAlertService apiAlertService,
            APIMonitorRepository apiMonitorRepository,
            AuthorityService authorityService,
            ServiceService serviceService) {
        super(authorityService, serviceService);
        this.API_ALERT_SERVICE = apiAlertService;
        this.API_MONITORING_TYPE_REPOSITORY = apiMonitorRepository;
    }


    @Override
    public List<APIMonitor> findAll() {
        return API_MONITORING_TYPE_REPOSITORY.findAll();
    }

    @Override
    protected APIMonitorRepository getRepository() {
        return API_MONITORING_TYPE_REPOSITORY;
    }

    @Override
    public APIMonitoringTask getScheduledTask(APIMonitor monitor) {
        if (monitor.getId().getService().isEnabled())
            return new APIMonitoringTask(API_ALERT_SERVICE, monitor);
        return null;
    }
}
