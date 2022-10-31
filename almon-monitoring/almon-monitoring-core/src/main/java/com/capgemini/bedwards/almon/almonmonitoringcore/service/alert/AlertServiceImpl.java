package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.AlertRepository;
import com.capgemini.bedwards.almon.notificationcore.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class AlertServiceImpl<T extends Alert> implements AlertService<T> {
    private final List<Notification> NOTIFICATIONS;

    private Set<Monitor> SENT_ALERTS;

    @Autowired
    public AlertServiceImpl(List<Notification> notifications) {
        this.NOTIFICATIONS = notifications;
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
        for (Notification notification : this.NOTIFICATIONS) {
            notification.sendNotification(alert);
        }
    }
}
