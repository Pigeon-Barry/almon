package com.capgemini.bedwards.almon.almonmonitoringcore.service;

import com.capgemini.bedwards.almon.almondatastore.models.ScheduledTask;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.ScheduledMonitoringType;

public interface ScheduledMonitorService<T extends ScheduledMonitoringType> extends MonitorService<T> {
    ScheduledTask getScheduledTask(T monitor);


}
