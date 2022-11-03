package com.capgemini.bedwards.almon.notificationcore;

import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.ServiceSubscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class Notifications {
    private final List<Notification> NOTIFICATIONS;
    private final UserService USER_SERVICE;

    @Autowired
    public Notifications(List<Notification> notifications, UserService userService) {
        this.NOTIFICATIONS = notifications;
        this.USER_SERVICE = userService;
    }

    public <T extends Alert> void send(T alert) {
        for (Notification notification : this.NOTIFICATIONS) {
            notification.sendNotification(alert);
        }
    }

    public List<Notification> getNotifications() {
        return this.NOTIFICATIONS;
    }


    private Optional<ServiceSubscription> getSubscription(User user, com.capgemini.bedwards.almon.almondatastore.models.service.Service service, Notification notification) {
        return user.getServiceSubscriptions().stream().filter(serviceSubscription ->
                serviceSubscription.getId().getService().equals(service) &&
                        serviceSubscription.getId().getNotificationType().equals(notification.getId())
        ).findFirst();
    }

    private Optional<MonitorSubscription> getSubscription(User user, Monitor monitor, Notification notification) {
        return user.getMonitorSubscriptions().stream().filter(monitorSubscription ->
                monitorSubscription.getId().getMonitor().equals(monitor) &&
                        monitorSubscription.getId().getNotificationType().equals(notification.getId())
        ).findFirst();
    }

    public boolean isUserSubscribedToNotificationAtServiceLevel(User user, Notification notification, com.capgemini.bedwards.almon.almondatastore.models.service.Service service) {
        Optional<ServiceSubscription> serviceSubscriptionOptional = getSubscription(user, service, notification);
        return serviceSubscriptionOptional.map(ServiceSubscription::isSubscribed).orElse(false);
    }

    public boolean isUserSubscribedToNotificationAtMonitorLevel(User user, Notification notification, Monitor monitor) {
        Optional<MonitorSubscription> serviceSubscriptionOptional = getSubscription(user, monitor, notification);
        return serviceSubscriptionOptional.map(MonitorSubscription::isSubscribed).orElse(false);
    }

    public boolean isUserSubscribedToNotification(User user, Notification notification, Monitor monitor) {
        return isUserSubscribedToNotificationAtServiceLevel(user, notification, monitor.getId().getService()) || isUserSubscribedToNotificationAtMonitorLevel(user, notification, monitor);
    }


}
