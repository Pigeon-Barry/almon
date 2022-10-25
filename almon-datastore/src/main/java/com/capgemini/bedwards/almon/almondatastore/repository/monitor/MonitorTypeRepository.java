package com.capgemini.bedwards.almon.almondatastore.repository.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorTypeRepository<T extends MonitoringType> extends JpaRepository<T, MonitoringType.MonitoringTypeId> {
}
