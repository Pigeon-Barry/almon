package com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MonitorTypeRepository<T extends Monitor> extends JpaRepository<T, Monitor.MonitorId> {


    @Query(
            value = "SELECT * FROM monitoring_type WHERE service_id=?1 AND id=?2",
            nativeQuery = true
    )
    Optional<Monitor> findByServiceKeyAndMonitorKey(String serviceId, String monitorId);


}


