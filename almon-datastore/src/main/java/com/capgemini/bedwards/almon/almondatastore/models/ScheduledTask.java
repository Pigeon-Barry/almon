package com.capgemini.bedwards.almon.almondatastore.models;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.ScheduledMonitoringType;
import lombok.Data;

@Data
public abstract class ScheduledTask implements Runnable {
    private final String TASK_ID;
    private String cronExpression;

    protected ScheduledTask(ScheduledMonitoringType scheduledMonitoringType) {
        this.TASK_ID = scheduledMonitoringType.getTaskId();
        this.cronExpression = scheduledMonitoringType.getCronExpression();
    }
}
