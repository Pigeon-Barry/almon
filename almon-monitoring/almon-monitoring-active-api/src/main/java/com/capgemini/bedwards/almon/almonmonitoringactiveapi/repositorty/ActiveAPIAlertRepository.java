package com.capgemini.bedwards.almon.almonmonitoringactiveapi.repositorty;

import com.capgemini.bedwards.almon.almondatastore.repository.alert.ScheduledAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIAlert;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveAPIAlertRepository extends ScheduledAlertRepository<ActiveAPIAlert> {
}
