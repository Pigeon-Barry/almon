package com.capgemini.bedwards.almon.almonmonitoringactiveapi.repositorty;

import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.ScheduledMonitorTypeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveAPIMonitorRepository extends ScheduledMonitorTypeRepository<ActiveAPIMonitor> {
}
