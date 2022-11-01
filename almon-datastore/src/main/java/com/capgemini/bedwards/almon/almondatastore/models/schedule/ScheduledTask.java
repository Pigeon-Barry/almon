package com.capgemini.bedwards.almon.almondatastore.models.schedule;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import lombok.Data;

@Data
public abstract class ScheduledTask<T extends ScheduledAlert> implements Runnable {
    protected final String TASK_ID;
    protected final String cronExpression;

    public abstract boolean isEnabled();


    public abstract Alert execute();

    protected ScheduledTask(ScheduledMonitor ScheduledMonitor) {
        this.TASK_ID = ScheduledMonitor.getTaskId();
        this.cronExpression = ScheduledMonitor.getCronExpression();
    }
    @Override
    public void run() {
        execute();
    }
}
