package com.capgemini.bedwards.almon.almondatastore.repository.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

    @Query("DELETE FROM Authority where authority like ?#{'SERVICE_' + #service.id + '_%'}")
    @Modifying
    @Transactional
    void deleteServiceAuthorities(@Param("service") Service service);


    //SERVICE_CBS_MONITOR_CBS-HEALTHCHECK_
    @Query("DELETE FROM Authority where authority like ?#{'SERVICE_' + #monitor.id.service.id + '_MONITOR_' + #monitor.id+'_%'}")
    @Modifying
    @Transactional
    void deleteMonitorAuthorities(@Param("monitor") Monitor monitor);
}
