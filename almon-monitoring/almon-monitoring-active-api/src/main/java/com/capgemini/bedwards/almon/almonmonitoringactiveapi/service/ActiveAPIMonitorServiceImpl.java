package com.capgemini.bedwards.almon.almonmonitoringactiveapi.service;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.repositorty.ActiveAPIMonitorRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorServiceBase;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActiveAPIMonitorServiceImpl extends ScheduledMonitorServiceBase<ActiveAPIMonitor> implements ActiveAPIMonitorService {

    private final ActiveAPIMonitorRepository API_MONITORING_TYPE_REPOSITORY;
    private final ActiveAPIAlertService API_ALERT_SERVICE;

    @Autowired
    public ActiveAPIMonitorServiceImpl(
            ActiveAPIAlertService activeApiAlertService,
            ActiveAPIMonitorRepository activeApiMonitorRepository,
            AuthorityService authorityService,
            ServiceService serviceService) {
        super(authorityService, serviceService);
        this.API_ALERT_SERVICE = activeApiAlertService;
        this.API_MONITORING_TYPE_REPOSITORY = activeApiMonitorRepository;
    }


    @Override
    public List<ActiveAPIMonitor> findAll() {
        return API_MONITORING_TYPE_REPOSITORY.findAll();
    }

    @Override
    protected ActiveAPIMonitorRepository getRepository() {
        return API_MONITORING_TYPE_REPOSITORY;
    }

    @Override
    public ActiveAPIMonitoringTask getScheduledTask(ActiveAPIMonitor monitor) {
        if (monitor.getId().getService().isEnabled()) {
            return new ActiveAPIMonitoringTask(API_ALERT_SERVICE, monitor);
        }
        return null;
    }
}
