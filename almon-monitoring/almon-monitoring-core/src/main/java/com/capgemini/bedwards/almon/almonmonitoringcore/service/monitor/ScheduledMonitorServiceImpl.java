package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.subscription.SubscriptionService;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.ScheduledMonitorTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ScheduledMonitorServiceImpl extends ScheduledMonitorServiceBase<ScheduledMonitor> implements MonitorService<ScheduledMonitor> {

    private final ScheduledMonitorTypeRepository<ScheduledMonitor> MONITORING_REPOSITORY;

    public ScheduledMonitorServiceImpl(ScheduledMonitorTypeRepository<ScheduledMonitor> monitorTypeRepository, AuthorityService authorityService, ServiceService serviceService, SubscriptionService subscriptionService) {
        super(authorityService, serviceService, subscriptionService);
        this.MONITORING_REPOSITORY = monitorTypeRepository;
    }

    @Override
    protected ScheduledMonitorTypeRepository<ScheduledMonitor> getRepository() {
        return MONITORING_REPOSITORY;
    }

}
