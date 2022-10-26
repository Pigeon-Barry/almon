package com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MonitorTypeRepository<T extends MonitoringType> extends JpaRepository<T, MonitoringType.MonitoringTypeId> {


    @Query(
            value = "SELECT * FROM monitoring_type WHERE service_id=?1 AND id=?2",
            nativeQuery = true
    )
    Optional<MonitoringType> findByServiceKeyAndMonitorKey(String serviceId, String monitorId);


}


