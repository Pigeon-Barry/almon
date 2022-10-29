package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitoringType;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;

;

public interface ScheduledMonitorService<T extends ScheduledMonitoringType> extends MonitorService<T> {
    ScheduledTask getScheduledTask(T monitor);


}
