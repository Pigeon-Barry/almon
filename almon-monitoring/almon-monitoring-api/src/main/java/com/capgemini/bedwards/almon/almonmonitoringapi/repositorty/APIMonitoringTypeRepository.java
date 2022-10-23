package com.capgemini.bedwards.almon.almonmonitoringapi.repositorty;

import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APIMonitoringTypeRepository extends JpaRepository<APIMonitoringType, String> {
}
