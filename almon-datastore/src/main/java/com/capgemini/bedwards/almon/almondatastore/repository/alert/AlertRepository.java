package com.capgemini.bedwards.almon.almondatastore.repository.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;


public interface AlertRepository<T extends Alert> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {


}
