package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Monitors {

    private final Map<String, MonitorType> MONITORING_TYPES;

    @Autowired
    public Monitors(List<MonitorType> monitoringTypes) {
        MONITORING_TYPES = new HashMap<>();
        monitoringTypes.forEach(monitoring -> MONITORING_TYPES.put(monitoring.getName(), monitoring));
    }

    public MonitorType getMonitorTypeFromName(String name) throws NotFoundException{
        MonitorType monitoring = MONITORING_TYPES.get(name);
        if (monitoring == null)
            throw new NotFoundException("No monitors found with the name '" + name + "'");
        return monitoring;
    }
}
