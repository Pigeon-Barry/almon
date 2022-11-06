package com.capgemini.bedwards.almon.almonmonitoringactiveapi.service;

import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorService;

import java.util.List;

public interface ActiveAPIMonitorService extends ScheduledMonitorService<ActiveAPIMonitor> {

    List<ActiveAPIMonitor> findAll();
    ActiveAPIMonitoringTask getScheduledTask(ActiveAPIMonitor monitor);

}
