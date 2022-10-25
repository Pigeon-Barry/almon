package com.capgemini.bedwards.almon.almondatastore.repository.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {


}
