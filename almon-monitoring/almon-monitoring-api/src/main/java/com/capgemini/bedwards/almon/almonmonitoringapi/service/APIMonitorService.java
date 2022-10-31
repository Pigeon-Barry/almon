package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorService;

import java.util.List;

public interface APIMonitorService extends ScheduledMonitorService<APIMonitor> {

    List<APIMonitor> findAll();
    APIMonitoringTask getScheduledTask(APIMonitor monitor);

}
