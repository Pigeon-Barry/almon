package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.ScheduledMonitorTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public final class ScheduledMonitorServiceImpl extends ScheduledMonitorServiceBase<ScheduledMonitor> implements MonitorService<ScheduledMonitor> {

    private final ScheduledMonitorTypeRepository<ScheduledMonitor> MONITORING_REPOSITORY;

    public ScheduledMonitorServiceImpl(ScheduledMonitorTypeRepository<ScheduledMonitor> monitorTypeRepository, AuthorityService authorityService, ServiceService serviceService) {
        super(authorityService, serviceService);
        this.MONITORING_REPOSITORY = monitorTypeRepository;
    }

    @Override
    protected ScheduledMonitorTypeRepository<ScheduledMonitor> getRepository() {
        return MONITORING_REPOSITORY;
    }

}
