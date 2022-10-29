package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitoringType;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.Scheduler;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.ScheduledMonitorTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.validation.constraints.NotNull;

;

@Slf4j
public abstract class ScheduledMonitorServiceBase<T extends ScheduledMonitoringType>
        extends MonitorServiceBase<T>
        implements ScheduledMonitorService<T> {

    @Autowired
    @Lazy
    protected Scheduler SCHEDULER;

    @Autowired
    public ScheduledMonitorServiceBase(
            AuthorityService authorityService,
            ServiceService serviceService) {
        super(authorityService, serviceService);
    }

    protected abstract ScheduledMonitorTypeRepository<T> getRepository();

    @Override
    public void enable(T monitor) {
        super.enable(monitor);
        SCHEDULER.scheduleTask(monitor.getScheduledTask());
    }


    @Override
    public void disable(T monitor) {
        super.disable(monitor);
        SCHEDULER.removeScheduledTask(monitor.getTaskId());
    }

    @Override
    protected void updateEnabledStatus(@NotNull T monitor, boolean enabled) {
        super.updateEnabledStatus(monitor, enabled);
        this.SCHEDULER.removeScheduledTask(monitor.getTaskId());
    }
}
