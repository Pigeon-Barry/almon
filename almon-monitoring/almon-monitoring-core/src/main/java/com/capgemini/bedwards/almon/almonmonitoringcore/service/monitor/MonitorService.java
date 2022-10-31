package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;

import java.lang.reflect.ParameterizedType;

public interface MonitorService<T extends Monitor> {
    void enable(T monitor);

    void disable(T monitor);

    T create(T monitorType);

    T save(T monitorType);

    T getMonitorFromCombinedId(String source);

    default Class<T> getSupportedMonitorType() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    void delete(T monitor);
}
