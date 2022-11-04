package com.capgemini.bedwards.almon.notificationcore;

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
public class NotificationHelper {

    private final List<Notification> NOTIFICATIONS;

    @Autowired
    public NotificationHelper(List<Notification> notifications) {
        this.NOTIFICATIONS = notifications;
    }

    public List<Notification> getNotifications() {
        return this.NOTIFICATIONS;
    }

    public Optional<ServiceSubscription> getSubscription(User user, com.capgemini.bedwards.almon.almondatastore.models.service.Service service, Notification notification) {
        return user.getServiceSubscriptions().stream().filter(serviceSubscription ->
                serviceSubscription.getId().getService().equals(service) &&
                        serviceSubscription.getId().getNotificationType().equals(notification.getId())
        ).findFirst();
    }

    public Optional<MonitorSubscription> getSubscription(User user, Monitor monitor, Notification notification) {
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
        if (getSubscription(user, monitor, notification).isPresent())
            return isUserSubscribedToNotificationAtMonitorLevel(user, notification, monitor);
        return isUserSubscribedToNotificationAtServiceLevel(user, notification, monitor.getId().getService());
    }

    public boolean isUserOverridingServiceSubscriptionForMonitor(User user, Monitor monitor) {
        return user.getMonitorSubscriptions().stream().anyMatch(monitorSubscription ->
                monitorSubscription.getId().getMonitor().equals(monitor)
        );
    }
}
