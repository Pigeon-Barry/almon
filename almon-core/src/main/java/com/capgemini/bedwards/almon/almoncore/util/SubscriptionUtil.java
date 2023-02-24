package com.capgemini.bedwards.almon.almoncore.util;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.ServiceSubscription;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SubscriptionUtil {
    public static <N extends Notification, S extends Service> Optional<ServiceSubscription> getSubscription(User user, S service, N notification) {
        return user.getServiceSubscriptions().stream().filter(serviceSubscription ->
                serviceSubscription.getId().getService().equals(service) &&
                        serviceSubscription.getId().getNotificationType().equals(notification.getId())
        ).findFirst();
    }

    public static <N extends Notification, M extends Monitor> Optional<MonitorSubscription> getSubscription(User user, M monitor, N notification) {
        return user.getMonitorSubscriptions().stream().filter(monitorSubscription ->
                monitorSubscription.getId().getMonitor().equals(monitor) &&
                        monitorSubscription.getId().getNotificationType().equals(notification.getId())
        ).findFirst();
    }

    public static <N extends Notification, S extends Service> boolean isUserSubscribedToNotificationAtServiceLevel(User user, N notification, S service) {
        Optional<ServiceSubscription> serviceSubscriptionOptional = getSubscription(user, service, notification);
        return serviceSubscriptionOptional.map(ServiceSubscription::isSubscribed).orElse(false);
    }

    public static <N extends Notification, M extends Monitor> boolean isUserSubscribedToNotificationAtMonitorLevel(User user, N notification, M monitor) {
        Optional<MonitorSubscription> serviceSubscriptionOptional = getSubscription(user, monitor, notification);
        return serviceSubscriptionOptional.map(MonitorSubscription::isSubscribed).orElse(false);
    }

    public static <N extends Notification, M extends Monitor> boolean isUserSubscribedToNotification(User user, N notification, M monitor) {
        if (getSubscription(user, monitor, notification).isPresent())
            return isUserSubscribedToNotificationAtMonitorLevel(user, notification, monitor);
        return isUserSubscribedToNotificationAtServiceLevel(user, notification, monitor.getId().getService());
    }

    public static <M extends Monitor> boolean isUserOverridingServiceSubscriptionForMonitor(User user, M monitor) {
        return user.getMonitorSubscriptions().stream().anyMatch(monitorSubscription ->
                monitorSubscription.getId().getMonitor().equals(monitor)
        );
    }
}
