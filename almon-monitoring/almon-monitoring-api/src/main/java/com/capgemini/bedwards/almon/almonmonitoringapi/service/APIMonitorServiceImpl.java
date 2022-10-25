package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almoncore.service.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringapi.repositorty.APIMonitoringTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.ScheduledTask;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class APIMonitorServiceImpl extends ScheduledMonitorServiceImpl<APIMonitoringType> implements APIMonitorService {

    private final APIMonitoringTypeRepository API_MONITORING_TYPE_REPOSITORY;
    private final APIAlertService API_ALERT_SERVICE;

    @Autowired
    public APIMonitorServiceImpl(
            APIAlertService apiAlertService,
            APIMonitoringTypeRepository apiMonitoringTypeRepository,
            AuthorityService authorityService,
            ServiceService serviceService) {
        super(authorityService, serviceService);
        this.API_ALERT_SERVICE = apiAlertService;
        this.API_MONITORING_TYPE_REPOSITORY = apiMonitoringTypeRepository;
    }


    @Override
    public List<APIMonitoringType> findAll() {
        return API_MONITORING_TYPE_REPOSITORY.findAll();
    }

    @Override
    protected APIMonitoringTypeRepository getRepository() {
        return API_MONITORING_TYPE_REPOSITORY;
    }

    @Override
    public ScheduledTask getScheduledTask(APIMonitoringType monitor) {
        return new APIMonitoringTask(API_ALERT_SERVICE, monitor);
    }
}
