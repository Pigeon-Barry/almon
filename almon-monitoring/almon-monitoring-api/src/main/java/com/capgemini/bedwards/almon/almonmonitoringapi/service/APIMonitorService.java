package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.ScheduledMonitorService;

import java.util.List;

public interface APIMonitorService extends ScheduledMonitorService<APIMonitoringType> {
    APIMonitoringType create(APIMonitoringType apiMonitoringType);

    List<APIMonitoringType> findAll();

}