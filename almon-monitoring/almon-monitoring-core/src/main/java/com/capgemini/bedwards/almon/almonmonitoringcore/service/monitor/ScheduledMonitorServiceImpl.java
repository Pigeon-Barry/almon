package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almoncore.service.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.ScheduledMonitoringType;
import com.capgemini.bedwards.almon.almondatastore.repository.monitor.ScheduledMonitorTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.ScheduledTask;
import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.validation.constraints.NotNull;

@Slf4j
public abstract class ScheduledMonitorServiceImpl<T extends ScheduledMonitoringType>
        extends MonitorServiceImpl<T>
        implements ScheduledMonitorService<T> {

    @Autowired
    @Lazy
    protected Scheduler SCHEDULER;

    @Autowired
    public ScheduledMonitorServiceImpl(
            AuthorityService authorityService,
            ServiceService serviceService) {
        super(authorityService, serviceService);
    }

    protected abstract ScheduledMonitorTypeRepository<T> getRepository();

    @Override
    public void enable(T monitor) {
        super.enable(monitor);
        this.SCHEDULER.scheduleTask(getScheduledTask(monitor));
    }

    public abstract ScheduledTask getScheduledTask(T monitor);


    @Override
    public void disable(T monitor) {
        super.disable(monitor);
        this.SCHEDULER.removeScheduledTask(monitor.getTaskId());
    }

    @Override
    protected void updateEnabledStatus(@NotNull T monitor, boolean enabled) {
        super.updateEnabledStatus(monitor, enabled);
        this.SCHEDULER.removeScheduledTask(monitor.getTaskId());
    }
}
