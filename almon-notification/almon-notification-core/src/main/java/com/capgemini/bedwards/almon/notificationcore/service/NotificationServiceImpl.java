package com.capgemini.bedwards.almon.notificationcore.service;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.notification.NotificationService;
import com.capgemini.bedwards.almon.almoncore.services.subscription.SubscriptionService;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    private final List<Notification> NOTIFICATIONS;
    private final SubscriptionService SUBSCRIPTION_SERVICE;

    @Autowired
    public NotificationServiceImpl(List<Notification> notifications, SubscriptionService subscriptionService) {
        this.NOTIFICATIONS = notifications;
        if (log.isInfoEnabled())
            for (Notification notification : notifications)
                log.info("Notification Type Identified: " + notification.getId());
        this.SUBSCRIPTION_SERVICE = subscriptionService;
    }

    public <T extends Alert<?>> void send(final T alert) {
        final Monitor monitor = alert.getMonitor();
        if (!alert.getStatus().shouldSendAlert() || monitor.getPreventNotificationUntil().isAfter(LocalDateTime.now())) {
            return;
        }
        monitor.setPreventNotificationUntil(LocalDateTime.now().plusSeconds(monitor.getNotificationThrottle()));
        sendNotification(alert);
    }

    private <T extends Alert<?>> void sendNotification(final T alert) {
        log.info("Sending Notifications for alert: " + alert);
        for (Notification notification : this.NOTIFICATIONS) {
            try {
                if (notification.isEnabled()) {
                    log.info("Sending notify request to notification: " + notification.getId());
                    notification.sendNotification(this.SUBSCRIPTION_SERVICE.getSubscribedUsers(alert, notification), alert);
                } else {
                    log.info("Notification: " + notification.getId() + " is disabled skipping");
                }
            } catch (Throwable throwable) {
                log.error("Failed to send notification using notification method: " + notification.getId(), throwable);
            }
        }
    }


    @Override
    public Notification getNotificationFromId(String source) {
        Optional<Notification> notificationOptional = this.NOTIFICATIONS.stream().filter(notification -> notification.getId().equals(source)).findFirst();
        if (notificationOptional.isPresent())
            return notificationOptional.get();
        throw new NotFoundException("Notification with id: '" + source + "' not found");
    }

    @Override
    public List<Notification> getNotificationTypes() {
        return this.NOTIFICATIONS;
    }
}
