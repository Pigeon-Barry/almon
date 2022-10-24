package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almondatastore.models.ScheduledTask;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringapi.repositorty.APIMonitoringTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.ScheduledMonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class APIMonitorServiceImpl extends ScheduledMonitorServiceImpl<APIMonitoringType> implements APIMonitorService {

    private final APIMonitoringTypeRepository API_MONITORING_TYPE_REPOSITORY;

    @Autowired
    public APIMonitorServiceImpl(APIMonitoringTypeRepository apiMonitoringTypeRepository) {
        this.API_MONITORING_TYPE_REPOSITORY = apiMonitoringTypeRepository;
    }

    @Override
    public APIMonitoringType create(APIMonitoringType apiMonitoringType) {
        return API_MONITORING_TYPE_REPOSITORY.save(apiMonitoringType);
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
        return new APIMonitoringTask(monitor);
    }
}
