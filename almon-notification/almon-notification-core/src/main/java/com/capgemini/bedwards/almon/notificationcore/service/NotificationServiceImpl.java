package com.capgemini.bedwards.almon.notificationcore.service;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.ServiceSubscription;
import com.capgemini.bedwards.almon.almondatastore.repository.subscription.MonitorSubscriptionRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.subscription.ServiceSubscriptionRepository;
import com.capgemini.bedwards.almon.notificationcore.NotificationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationHelper NOTIFICATION_HELPER;
    private final ServiceSubscriptionRepository SERVICE_SUBSCRIPTION_REPOSITORY;
    private final MonitorSubscriptionRepository MONITOR_SUBSCRIPTION_REPOSITORY;
    private final List<Notification> NOTIFICATIONS;

    @Autowired
    public NotificationServiceImpl(List<Notification> notifications, ServiceSubscriptionRepository serviceSubscriptionRepository, MonitorSubscriptionRepository monitorSubscriptionRepository) {
        this.NOTIFICATIONS = notifications;
        if (log.isInfoEnabled())
            for (Notification notification : notifications)
                log.info("Notification Identified: " + notification.getId());
        this.SERVICE_SUBSCRIPTION_REPOSITORY = serviceSubscriptionRepository;
        this.MONITOR_SUBSCRIPTION_REPOSITORY = monitorSubscriptionRepository;
        this.NOTIFICATION_HELPER = new NotificationHelper(this.NOTIFICATIONS);
    }

    @Override
    public boolean unsubscribe(User user, com.capgemini.bedwards.almon.almondatastore.models.service.Service service, Notification notification) {
        Optional<ServiceSubscription> serviceSubscriptionOptional = this.getNotificationHelper().getSubscription(user, service, notification);
        if (serviceSubscriptionOptional.isPresent()) {
            ServiceSubscription serviceSubscription = serviceSubscriptionOptional.get();
            if (serviceSubscription.isSubscribed()) {
                serviceSubscription.setSubscribed(false);
                this.SERVICE_SUBSCRIPTION_REPOSITORY.save(serviceSubscription);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean subscribe(User user, com.capgemini.bedwards.almon.almondatastore.models.service.Service service, Notification notification) {
        Optional<ServiceSubscription> serviceSubscriptionOptional = this.getNotificationHelper().getSubscription(user, service, notification);
        ServiceSubscription serviceSubscription;
        if (!serviceSubscriptionOptional.isPresent()) {
            serviceSubscription = new ServiceSubscription(new ServiceSubscription.SubscriptionId(notification.getId(), service, user), false);
            user.getServiceSubscriptions().add(serviceSubscription);
        } else
            serviceSubscription = serviceSubscriptionOptional.get();

        if (!serviceSubscription.isSubscribed()) {
            serviceSubscription.setSubscribed(true);
            this.SERVICE_SUBSCRIPTION_REPOSITORY.save(serviceSubscription);
            return true;
        }
        return false;
    }

    @Override
    public boolean unsubscribe(User user, Monitor monitor, Notification notification) {
        Optional<MonitorSubscription> monitorSubscriptionOptional = this.getNotificationHelper().getSubscription(user, monitor, notification);
        MonitorSubscription monitorSubscription;

        if (!monitorSubscriptionOptional.isPresent()) {
            monitorSubscription = new MonitorSubscription(new MonitorSubscription.SubscriptionId(notification.getId(), monitor, user), false);
            user.getMonitorSubscriptions().add(monitorSubscription);
        } else {
            monitorSubscription = monitorSubscriptionOptional.get();
            if (!monitorSubscription.isSubscribed())
                return false;
        }
        monitorSubscription.setSubscribed(false);
        this.MONITOR_SUBSCRIPTION_REPOSITORY.save(monitorSubscription);
        return true;
    }

    @Override
    public boolean subscribe(User user, Monitor monitor, Notification notification) {
        Optional<MonitorSubscription> monitorSubscriptionOptional = this.getNotificationHelper().getSubscription(user, monitor, notification);
        MonitorSubscription monitorSubscription;
        if (!monitorSubscriptionOptional.isPresent()) {
            monitorSubscription = new MonitorSubscription(new MonitorSubscription.SubscriptionId(notification.getId(), monitor, user), false);
            user.getMonitorSubscriptions().add(monitorSubscription);
        } else
            monitorSubscription = monitorSubscriptionOptional.get();

        if (!monitorSubscription.isSubscribed()) {
            monitorSubscription.setSubscribed(true);
            this.MONITOR_SUBSCRIPTION_REPOSITORY.save(monitorSubscription);
            return true;
        }
        return false;
    }


    @Override
    public NotificationHelper getNotificationHelper() {
        return this.NOTIFICATION_HELPER;
    }

    public <T extends Alert> void send(T alert) {
        log.info("Sending Notifications for alert: " + alert);
        for (Notification notification : this.NOTIFICATIONS) {
            notification.sendNotification(getSubscribedUsers(alert, notification), alert);
        }
    }

    private <T extends Alert> Set<User> getSubscribedUsers(T alert, Notification notification) {
        List<MonitorSubscription> monitorSubscriptionList = MONITOR_SUBSCRIPTION_REPOSITORY.getFromNotificationId(notification.getId(), alert.getMonitor());
        Set<User> monitorSubscriptionUsers = monitorSubscriptionList.stream().map(monitorSubscription -> monitorSubscription.getId().getUser()).collect(Collectors.toSet());

        List<ServiceSubscription> serviceSubscriptionList;
        if (monitorSubscriptionUsers.size() > 0)
            serviceSubscriptionList = SERVICE_SUBSCRIPTION_REPOSITORY.getFromNotificationIdWhereNotUser(notification.getId(), monitorSubscriptionUsers);
        else
            serviceSubscriptionList = SERVICE_SUBSCRIPTION_REPOSITORY.getFromNotificationId(notification.getId());
        Set<User> users = monitorSubscriptionList.stream().filter(MonitorSubscription::isSubscribed).map(monitorSubscription -> monitorSubscription.getId().getUser()).collect(Collectors.toSet());
        users.addAll(serviceSubscriptionList.stream().filter(ServiceSubscription::isSubscribed).map(serviceSubscription -> serviceSubscription.getId().getUser()).collect(Collectors.toSet()));
        return users;
    }

    @Override
    public Notification getNotificationFromId(String source) {
        Optional<Notification> notificationOptional = this.NOTIFICATIONS.stream().filter(notification -> notification.getId().equals(source)).findFirst();
        if (notificationOptional.isPresent())
            return notificationOptional.get();
        throw new NotFoundException("Notification with id: '" + source + "' not found");
    }

    @Override
    public void clearSubscriptions(User user, Monitor monitor) {
        this.MONITOR_SUBSCRIPTION_REPOSITORY.deleteById_UserAndId_Monitor(user, monitor);
        user.getMonitorSubscriptions().clear();
    }
}
