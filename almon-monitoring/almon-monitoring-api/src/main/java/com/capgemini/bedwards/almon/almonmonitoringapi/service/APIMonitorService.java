package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorService;

import java.util.List;

public interface APIMonitorService extends ScheduledMonitorService<APIMonitoringType> {

    List<APIMonitoringType> findAll();
    APIMonitoringTask getScheduledTask(APIMonitoringType monitor);

}
