package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringapi.repositorty.APIMonitoringTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class APIMonitorServiceImpl implements APIMonitorService {

    private final APIMonitoringTypeRepository API_MONITORING_TYPE_REPOSITORY;

    public APIMonitorServiceImpl(APIMonitoringTypeRepository apiMonitoringTypeRepository){
        this.API_MONITORING_TYPE_REPOSITORY = apiMonitoringTypeRepository;
    }
    @Override
    public APIMonitoringType create(APIMonitoringType apiMonitoringType) {
        return API_MONITORING_TYPE_REPOSITORY.save(apiMonitoringType);
    }
}
