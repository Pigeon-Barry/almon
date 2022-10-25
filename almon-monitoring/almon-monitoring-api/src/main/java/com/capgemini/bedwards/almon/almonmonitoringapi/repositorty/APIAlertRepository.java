package com.capgemini.bedwards.almon.almonmonitoringapi.repositorty;

import com.capgemini.bedwards.almon.almondatastore.repository.alert.ScheduledAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIAlertType;
import org.springframework.stereotype.Repository;

@Repository
public interface APIAlertRepository extends ScheduledAlertRepository<APIAlertType> {
}
