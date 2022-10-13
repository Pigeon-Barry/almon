package com.capgemini.bedwards.almon.almondatastore.repository;

import com.capgemini.bedwards.almon.almondatastore.models.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Authority.AuthorityId> {
}