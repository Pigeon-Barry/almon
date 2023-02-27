package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.Scheduler;
import org.springframework.boot.test.mock.mockito.MockBean;


public abstract class ScheduledMonitorServiceBaseTest<T extends ScheduledMonitor> extends MonitorServiceBaseTest<T> {

    @MockBean
    protected Scheduler scheduler;


}
