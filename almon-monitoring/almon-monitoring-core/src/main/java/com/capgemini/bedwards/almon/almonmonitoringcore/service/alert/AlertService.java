package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertSpecification;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface AlertService<T extends Alert<?>> {

  T create(T monitorType);

  void sendAlert(T alert);

  Page<T> getAlertsPaginated(AlertSpecification<T> specification, int pageNumber, int pageSize);

  T getAlertFromId(UUID id);
}
