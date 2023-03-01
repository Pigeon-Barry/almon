package com.capgemini.bedwards.almon.almonaccessapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;


public class TestScheduledTask extends ScheduledTask<TestScheduledAlert> {

    public TestScheduledTask(TestScheduledMonitor testScheduledMonitor) {
        super(testScheduledMonitor);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Alert execute() {
        return null;
    }
}
