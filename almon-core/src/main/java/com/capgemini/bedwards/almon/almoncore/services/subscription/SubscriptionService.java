package com.capgemini.bedwards.almon.almoncore.services.subscription;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;

import java.util.Set;

public interface SubscriptionService {

    boolean unsubscribe(User user, Service service, Notification notification);

    boolean subscribe(User user, Service service, Notification notification);

    boolean unsubscribe(User user, Monitor monitor, Notification notification);

    boolean subscribe(User user, Monitor monitor, Notification notification);

    <T extends Alert<?>> Set<User> getSubscribedUsers(T alert, Notification notification);

    void clearSubscriptions(User authenticatedUser, Monitor monitor);

    void clearSubscriptions(Service service);

    void clearSubscriptions(Monitor monitor);
}
