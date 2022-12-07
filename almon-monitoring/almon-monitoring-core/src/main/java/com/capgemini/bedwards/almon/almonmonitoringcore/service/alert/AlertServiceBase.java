package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertSpecification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.AlertRepository;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Slf4j
public abstract class AlertServiceBase<T extends Alert<?>> implements AlertService<T> {


  private final NotificationService NOTIFICATION_SERVICE;
  private Set<Monitor> SENT_ALERTS;

  @Autowired
  public AlertServiceBase(NotificationService notificationService) {
    this.NOTIFICATION_SERVICE = notificationService;
    this.SENT_ALERTS = new HashSet<>();
  }


  protected abstract AlertRepository<T> getRepository();

  @Override
  public T create(T alert) {
    log.info("Saving alert: " + alert);
    alert = getRepository().save(alert);
    if (alert.getStatus().shouldSendAlert()) {
      if (!SENT_ALERTS.contains(alert.getMonitor())) {
        SENT_ALERTS.add(alert.getMonitor());
        sendAlert(alert);
      }
    } else {
      SENT_ALERTS.remove(alert.getMonitor());
    }
    return alert;
  }

  public void sendAlert(T alert) {
    this.NOTIFICATION_SERVICE.send(alert);
  }

  @Override
  public Page<T> getAlertsPaginated(AlertSpecification<T> specification, int pageNumber,
      int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    return getRepository().findAll(specification, pageable);
  }


  @Override
  public List<T> getAlerts(AlertSpecification<T> alertSpecification) {
    return getAlerts(alertSpecification, Sort.by(Direction.ASC, "createdAt"));
  }

  @Override
  public List<T> getAlerts(AlertSpecification<T> alertSpecification, Sort sort) {
    return getRepository().findAll(alertSpecification, sort);
  }


  @Override
  public T getAlertFromId(UUID id) {
    return getAlertFromId(id);
  }
}
