package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.repository;

import com.capgemini.bedwards.almon.almondatastore.repository.alert.AlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIAlert;
import org.springframework.stereotype.Repository;

@Repository
public interface PassiveAPIAlertRepository extends AlertRepository<PassiveAPIAlert> {
}
