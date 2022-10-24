package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorType;
import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Monitors {
    @Autowired
    Scheduler scheduler;
    private final Map<String, MonitorType> MONITORING_TYPES;

    @Autowired
    public Monitors(List<MonitorType> monitoringTypes) {
        MONITORING_TYPES = new HashMap<>();
        monitoringTypes.forEach(monitoring -> MONITORING_TYPES.put(monitoring.getId(), monitoring));
    }

    public MonitorType getMonitorTypeFromId(String id) throws NotFoundException {
        MonitorType monitoring = MONITORING_TYPES.get(id);
        if (monitoring == null)
            throw new NotFoundException("No monitors found with the id '" + id + "'");
        return monitoring;
    }

    public Collection<MonitorType> getMonitorTypes() {
        return this.MONITORING_TYPES.values();
    }
}
