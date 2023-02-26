package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.notification.NotificationService;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertFilterOptions;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertSpecification;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.AlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public abstract class AlertServiceBase<T extends Alert<?>> implements AlertService<T> {


    private final NotificationService NOTIFICATION_SERVICE;

    @Autowired
    public AlertServiceBase(NotificationService notificationService) {
        this.NOTIFICATION_SERVICE = notificationService;
    }


    protected abstract AlertRepository<T> getRepository();

    @Override
    @Transactional
    public T create(T alert) {
        log.info("Saving alert: " + alert);
        alert = getRepository().save(alert);
        sendAlert(alert);
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
        Optional<T> alertOptional = getRepository().findById(id);
        if (alertOptional.isPresent())
            return alertOptional.get();
        throw new NotFoundException("Failed to find alert with id: '" + id + "'");
    }

    @Override
    public Map<String, Long> getAlertCountByStatus(Service service, LocalDateTime from, LocalDateTime to) {
        return getAlertCountByStatus(service.getMonitors().toArray(new Monitor[0]), from, to);
    }

    @Override
    public Map<String, Map<String, Object>> getAlertStatusByService(List<Service> services, LocalDateTime from, LocalDateTime to) {
        Map<String, Map<String, Object>> alertStatusMap = new HashMap<>();
        for (Service service : services) {
            Map<String, Object> serviceStatsMap = new HashMap<>();
            AtomicInteger totalCount = new AtomicInteger();
            getAlertCountByStatus(service, from, to).forEach((status, count) -> {
                serviceStatsMap.put(status, count);
                totalCount.addAndGet(count.intValue());
            });
            serviceStatsMap.put("total", totalCount.get());
            serviceStatsMap.put("failureRate", totalCount.get() == 0L ? 0.0 : ((double) ((totalCount.get() - (long) serviceStatsMap.get(Status.PASS.toString()))) / (double) totalCount.get()) * 100.0);
            alertStatusMap.put(service.getId(), serviceStatsMap);
        }
        return alertStatusMap;
    }

    @Override
    public Map<String, Long> getAlertCountByStatus(Monitor monitor, LocalDateTime from, LocalDateTime to) {
        return getAlertCountByStatus(new Monitor[]{monitor}, from, to);
    }

    public Map<String, Long> getAlertCountByStatus(Monitor[] monitors, LocalDateTime from, LocalDateTime to) {
        return Arrays.stream(Status.values()).collect(
                Collectors.toMap(Status::toString, status -> getRepository().count(new AlertSpecification<>(
                        AlertFilterOptions.builder()
                                .from(from)
                                .to(to)
                                .monitors(monitors)
                                .status(new Status[]{status})
                                .build())))
        );
    }
}
