package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.repository;

import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.MonitorTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIMonitor;
import org.springframework.stereotype.Repository;

@Repository
public interface PassiveAPIMonitorRepository extends MonitorTypeRepository<PassiveAPIMonitor> {
}
