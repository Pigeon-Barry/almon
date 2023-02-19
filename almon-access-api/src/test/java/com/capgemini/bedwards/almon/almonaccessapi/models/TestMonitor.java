package com.capgemini.bedwards.almon.almonaccessapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TestMonitor extends Monitor {
    @Override
    public String getMonitorType() {
        return "TEST";
    }
}
