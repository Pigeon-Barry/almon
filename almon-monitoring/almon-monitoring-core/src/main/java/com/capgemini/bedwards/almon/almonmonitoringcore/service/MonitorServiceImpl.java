package com.capgemini.bedwards.almon.almonmonitoringcore.service;

import com.capgemini.bedwards.almon.almoncore.service.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almondatastore.repository.monitor.MonitorTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Collections;

@Slf4j
public abstract class MonitorServiceImpl<T extends MonitoringType> implements MonitorService<T> {


    protected final AuthorityService AUTHORITY_SERVICE;
    protected final ServiceService SERVICE_SERVICE;

    @Autowired
    public MonitorServiceImpl(AuthorityService authorityService, ServiceService serviceService) {
        this.AUTHORITY_SERVICE = authorityService;
        this.SERVICE_SERVICE = serviceService;
    }

    protected abstract MonitorTypeRepository<T> getRepository();

    @Override
    public void enable(T monitor) {
        updateEnabledStatus(monitor, true);
    }

    @Override
    public void disable(T monitor) {
        updateEnabledStatus(monitor, false);
    }

    @Override
    public T create(T monitorType) {
        monitorType = save(monitorType);
        if (monitorType.isEnabled())
            enable(monitorType);
        AUTHORITY_SERVICE.addRole(
                AUTHORITY_SERVICE.createAuthority(
                        "MONITOR_" + monitorType.getId() + "_CAN_ENABLE_DISABLE",
                        "Grants the ability to enable/disable this monitor"
                ),
                Collections.singleton(SERVICE_SERVICE.getOrCreateAdminRole(monitorType.getId().getService()))
        );
        return monitorType;
    }

    @Override
    public T save(T monitorType) {
        return getRepository().save(monitorType);
    }

    protected void updateEnabledStatus(@NotNull T monitor, boolean enabled) {
        log.info((enabled ? "Enabling" : "Disabling") + " monitor: " + monitor);
        monitor.setEnabled(enabled);
        getRepository().save(monitor);
    }
}
