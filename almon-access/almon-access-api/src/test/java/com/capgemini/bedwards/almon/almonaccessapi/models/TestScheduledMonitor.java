package com.capgemini.bedwards.almon.almonaccessapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TestScheduledMonitor extends ScheduledMonitor {
    @Override
    public String getMonitorType() {
        return "TEST";
    }

    @Override
    public ScheduledTask getScheduledTask() {
        return new TestScheduledTask(this);
    }
}
