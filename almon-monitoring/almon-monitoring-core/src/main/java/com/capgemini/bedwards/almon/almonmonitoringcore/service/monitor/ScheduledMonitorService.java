package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.ScheduledMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.ScheduledTask;

public interface ScheduledMonitorService<T extends ScheduledMonitoringType> extends MonitorService<T> {
    ScheduledTask getScheduledTask(T monitor);


}
