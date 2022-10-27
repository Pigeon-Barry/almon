package com.capgemini.bedwards.almon.almondatastore.repository.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("DELETE FROM Role where name like ?#{'SERVICE_' + #service.id + '_%'}")
    @Modifying
    @Transactional
    void deleteServiceRoles(@Param("service") Service service);
}
