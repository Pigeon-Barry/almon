package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.subscription.SubscriptionService;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.MonitorTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MonitorServiceImpl extends MonitorServiceBase<Monitor> implements MonitorService<Monitor> {

    private final MonitorTypeRepository<Monitor> MONITORING_REPOSITORY;

    public MonitorServiceImpl(MonitorTypeRepository<Monitor> monitorTypeRepository, AuthorityService authorityService, ServiceService serviceService, SubscriptionService subscriptionService) {
        super(authorityService, serviceService, subscriptionService);
        this.MONITORING_REPOSITORY = monitorTypeRepository;
    }


    @Override
    protected MonitorTypeRepository<Monitor> getRepository() {
        return MONITORING_REPOSITORY;
    }

}
