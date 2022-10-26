package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;

public interface MonitorService<T extends MonitoringType> {
    void enable(T monitor);
    void disable(T monitor);

    T create(T monitorType);

    T save(T monitorType);

    T getMonitorFromCombinedId(String source);

}
