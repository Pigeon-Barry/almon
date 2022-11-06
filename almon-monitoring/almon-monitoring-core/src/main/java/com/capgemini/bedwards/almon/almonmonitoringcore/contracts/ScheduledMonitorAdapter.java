package com.capgemini.bedwards.almon.almonmonitoringcore.contracts;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;


public interface ScheduledMonitorAdapter<T extends ScheduledMonitor, A extends Alert> extends MonitorAdapter<T, A> {

    A execute(T monitor);
}
