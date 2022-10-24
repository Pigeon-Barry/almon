package com.capgemini.bedwards.almon.almonmonitoringapi.repositorty;

import com.capgemini.bedwards.almon.almondatastore.repository.monitor.ScheduledMonitorTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import org.springframework.stereotype.Repository;

@Repository
public interface APIMonitoringTypeRepository extends ScheduledMonitorTypeRepository<APIMonitoringType> {
}