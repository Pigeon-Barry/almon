package com.capgemini.bedwards.almon.almonmonitoringapi.monitoring;

import com.capgemini.bedwards.almon.almonmonitoringcore.MonitorType;
import org.springframework.stereotype.Component;

@Component
public class APIMonitor implements MonitorType {
    @Override
    public String getName() {
        return "ACTIVE_API";
    }
}
