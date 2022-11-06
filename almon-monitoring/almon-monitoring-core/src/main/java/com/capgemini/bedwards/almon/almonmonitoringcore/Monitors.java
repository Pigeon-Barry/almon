package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class Monitors {
    private final Map<String, MonitorAdapter> MONITOR_ADAPTERS;
    private final Map<Class<? extends Monitor>, MonitorService<? extends Monitor>> MONITOR_SERVICE;

    @Autowired
    public Monitors(List<MonitorAdapter> monitorAdapterList, List<MonitorService<?>> monitorServices) {
        MONITOR_ADAPTERS = new HashMap<>();
        MONITOR_SERVICE = new HashMap<>();
        monitorAdapterList.forEach(monitorAdapter -> {
            log.info("Monitor Adapter Identified: " + monitorAdapter.getId() + ": " + monitorAdapter.getClass());
            MONITOR_ADAPTERS.put(monitorAdapter.getId(), monitorAdapter);
        });
        monitorServices.forEach(monitorService -> MONITOR_SERVICE.put(monitorService.getSupportedMonitorType(), monitorService));
    }

    public MonitorAdapter getMonitorAdapterFromId(String id) throws NotFoundException {
        MonitorAdapter monitorAdapter = MONITOR_ADAPTERS.get(id);
        if (monitorAdapter == null)
            throw new NotFoundException("No monitors found with the id '" + id + "'");
        return monitorAdapter;
    }

    public Collection<MonitorAdapter> getMonitorAdapters() {
        return this.MONITOR_ADAPTERS.values();
    }

    public MonitorAdapter getMonitorAdapterFromMonitor(Monitor monitor) {
        Optional<MonitorAdapter> monitorTypeOptional = MONITOR_ADAPTERS.values().stream().filter(monitorType -> monitorType.getMonitorClass().equals(monitor.getClass())).findFirst();
        if (!monitorTypeOptional.isPresent())
            throw new NotFoundException("No monitors found that handles '" + monitor.getClass() + "' Monitoring Type");
        return monitorTypeOptional.get();
    }

    public <T extends Monitor> MonitorService<T> getMonitorServiceFromMonitor(T monitor) {
        Optional<? extends MonitorService> monitorServiceOptional = MONITOR_SERVICE.values().stream().filter(monitorService -> monitorService.getSupportedMonitorType().equals(monitor.getClass())).findFirst();
        if (!monitorServiceOptional.isPresent())
            throw new NotFoundException("No monitors found that handles '" + monitor.getClass() + "' Monitoring Type");
        return monitorServiceOptional.get();
    }
}
