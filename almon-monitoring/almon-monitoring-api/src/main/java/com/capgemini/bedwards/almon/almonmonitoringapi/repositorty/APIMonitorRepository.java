package com.capgemini.bedwards.almon.almonmonitoringapi.repositorty;

import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.ScheduledMonitorTypeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APIMonitorRepository extends ScheduledMonitorTypeRepository<APIMonitor> {
}
