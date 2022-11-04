package com.capgemini.bedwards.almon.almonmonitoringcore.models;

import com.capgemini.bedwards.almon.almoncore.validators.CronExpression;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CreateScheduledMonitorRequestBody extends CreateMonitorRequestBody {
    @CronExpression
    protected String cronExpression;


    protected <T extends ScheduledMonitor.ScheduledMonitorBuilder<?, ?>> T toScheduledMonitor(T builder, Service service) {
        builder = toMonitor(builder, service);
        builder.cronExpression(this.getCronExpression());

        return builder;
    }
}
