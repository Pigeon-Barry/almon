package com.capgemini.bedwards.almon.almondatastore.models.schedule;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import lombok.Data;

@Data
public abstract class ScheduledTask<T extends ScheduledAlert> implements Runnable {
    protected final String TASK_ID;
    protected final String cronExpression;

    public abstract boolean isEnabled();


    protected ScheduledTask(ScheduledMonitoringType scheduledMonitoringType) {
        this.TASK_ID = scheduledMonitoringType.getTaskId();
        this.cronExpression = scheduledMonitoringType.getCronExpression();
    }

}
