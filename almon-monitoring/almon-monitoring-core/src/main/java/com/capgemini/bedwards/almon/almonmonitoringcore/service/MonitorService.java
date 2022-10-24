package com.capgemini.bedwards.almon.almonmonitoringcore.service;

public interface MonitorService<T> {
    void enable(T monitor);
    void disable(T monitor);
}
