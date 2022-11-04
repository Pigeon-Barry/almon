package com.capgemini.bedwards.almon.almonmonitoringcore.models;

import com.capgemini.bedwards.almon.almoncore.validators.CronExpression;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class UpdateScheduledMonitorRequestBody extends UpdateMonitorRequestBody {
    @CronExpression
    protected String cronExpression;


    protected <T extends ScheduledMonitor> T toScheduledMonitor(T monitor) {
        toMonitor(monitor);
        if (this.getCronExpression() != null)
            monitor.setCronExpression(this.getCronExpression());
        return monitor;
    }

    protected UpdateScheduledMonitorRequestBody populate(ScheduledMonitor monitor) {
        super.populate(monitor);
        this.setCronExpression(monitor.getCronExpression());
        return this;
    }
}
