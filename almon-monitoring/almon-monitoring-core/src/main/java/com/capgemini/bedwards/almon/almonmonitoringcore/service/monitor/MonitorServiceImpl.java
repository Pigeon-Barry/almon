package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.MonitorTypeRepository;
import org.springframework.stereotype.Service;

@Service
public final class MonitorServiceImpl extends MonitorServiceBase<MonitoringType> implements MonitorService<MonitoringType> {

    private final MonitorTypeRepository<MonitoringType> MONITORING_REPOSITORY;

    public MonitorServiceImpl(MonitorTypeRepository<MonitoringType> monitorTypeRepository, AuthorityService authorityService, ServiceService serviceService) {
        super(authorityService, serviceService);
        this.MONITORING_REPOSITORY = monitorTypeRepository;
    }


    @Override
    protected MonitorTypeRepository<MonitoringType> getRepository() {
        return MONITORING_REPOSITORY;
    }
}
