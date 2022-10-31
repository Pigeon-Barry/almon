package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;

;

public interface ScheduledMonitorService<T extends ScheduledMonitor> extends MonitorService<T> {
    ScheduledTask getScheduledTask(T monitor);


}
