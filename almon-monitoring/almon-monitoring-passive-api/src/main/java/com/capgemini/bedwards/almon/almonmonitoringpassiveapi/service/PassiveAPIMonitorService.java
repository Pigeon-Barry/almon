package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.service;

import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIAlert;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.TriggerPassiveAPIMonitorRequestBody;

import java.util.List;

public interface PassiveAPIMonitorService extends MonitorService<PassiveAPIMonitor> {

    List<PassiveAPIMonitor> findAll();

    PassiveAPIAlert createAlert(TriggerPassiveAPIMonitorRequestBody body);
}
