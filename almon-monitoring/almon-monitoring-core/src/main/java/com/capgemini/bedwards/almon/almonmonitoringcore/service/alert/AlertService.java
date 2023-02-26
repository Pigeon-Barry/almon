package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertSpecification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AlertService<T extends Alert<?>> {

  T create(T monitorType);

  void sendAlert(T alert);

  Page<T> getAlertsPaginated(AlertSpecification<T> specification, int pageNumber, int pageSize);

  T getAlertFromId(UUID id);

  List<T> getAlerts(AlertSpecification<T> alertSpecification);


  List<T> getAlerts(AlertSpecification<T> alertSpecification, Sort sort);

  Map<String, Long> getAlertCountByStatus(Service service, LocalDateTime from, LocalDateTime to);

  Map<String, Map<String, Object>> getAlertStatusByService(List<Service> services, LocalDateTime from, LocalDateTime to);

  Map<String, Long> getAlertCountByStatus(Monitor monitor, LocalDateTime from, LocalDateTime to);
}
