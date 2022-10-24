package com.capgemini.bedwards.almon.almonmonitoringcore.service;

import com.capgemini.bedwards.almon.almondatastore.models.ScheduledTask;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.ScheduledMonitoringType;
import com.capgemini.bedwards.almon.almondatastore.repository.monitor.ScheduledMonitorTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.validation.constraints.NotNull;

@Slf4j
public abstract class ScheduledMonitorServiceImpl<T extends ScheduledMonitoringType> implements ScheduledMonitorService<T> {

    @Autowired
    @Lazy
    protected Scheduler SCHEDULER;

    public ScheduledMonitorServiceImpl() {
    }

    protected abstract ScheduledMonitorTypeRepository<T> getRepository();

    @Override
    public void enable(T monitor) {
        updateEnabledStatus(monitor, true);
        this.SCHEDULER.scheduleTask(getScheduledTask(monitor));
    }

    public abstract ScheduledTask getScheduledTask(T monitor);

    private void updateEnabledStatus(@NotNull T monitor, boolean enabled) {
        log.info((enabled ? "Enabling" : "Disabling") + " monitor: " + monitor);
        monitor.setEnabled(enabled);
        getRepository().save(monitor);
        this.SCHEDULER.removeScheduledTask(monitor.getTaskId());
    }

    @Override
    public void disable(T monitor) {
        updateEnabledStatus(monitor, false);
    }
}
