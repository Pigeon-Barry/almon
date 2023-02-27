package com.capgemini.bedwards.almon.almoncore.services.subscription;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.ServiceSubscription;
import com.capgemini.bedwards.almon.almondatastore.repository.subscription.MonitorSubscriptionRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.subscription.ServiceSubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                SubscriptionServiceImpl.class
        }
)
public class SubscriptionServiceImplTest {

    @MockBean
    private ServiceSubscriptionRepository serviceSubscriptionRepository;
    @MockBean
    private MonitorSubscriptionRepository monitorSubscriptionRepository;

    @Autowired
    private SubscriptionServiceImpl subscriptionService;


    @Test
    public void positive_unsubscribe_service_no_subscriptions() {
        Notification notification = mock(Notification.class);
        Service service = Service.builder().build();
        User user = User.builder().serviceSubscriptions(new HashSet<>()).build();

        assertFalse(subscriptionService.unsubscribe(user, service, notification));
        verify(serviceSubscriptionRepository, times(0)).save(any());
    }

    @Test
    public void positive_unsubscribe_service_user_is_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Service service = Service.builder().build();
        ServiceSubscription serviceSubscription = ServiceSubscription.builder()
                .id(ServiceSubscription.SubscriptionId.builder().service(service).notificationType("SMS").build())
                .subscribed(true)
                .build();
        User user = User.builder().serviceSubscriptions(new HashSet<ServiceSubscription>() {{
            add(serviceSubscription);
        }}).build();

        assertTrue(subscriptionService.unsubscribe(user, service, notification));
        verify(serviceSubscriptionRepository, times(1)).save(eq(serviceSubscription));
        assertFalse(serviceSubscription.isSubscribed());
    }

    @Test
    public void positive_unsubscribe_service_user_is_not_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Service service = Service.builder().build();
        ServiceSubscription serviceSubscription = ServiceSubscription.builder()
                .id(ServiceSubscription.SubscriptionId.builder().service(service).notificationType("SMS").build())
                .subscribed(false)
                .build();
        User user = User.builder().serviceSubscriptions(new HashSet<ServiceSubscription>() {{
            add(serviceSubscription);
        }}).build();

        assertFalse(subscriptionService.unsubscribe(user, service, notification));
        verify(serviceSubscriptionRepository, times(0)).save(any());
    }

    @Test
    public void positive_subscribe_service_no_subscriptions() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Service service = Service.builder().build();
        User user = User.builder().id(UUID.randomUUID()).serviceSubscriptions(new HashSet<>()).build();

        ArgumentCaptor<ServiceSubscription> serviceSubscriptionArgumentCaptor = ArgumentCaptor.forClass(ServiceSubscription.class);

        assertTrue(subscriptionService.subscribe(user, service, notification));
        verify(serviceSubscriptionRepository, times(1)).save(serviceSubscriptionArgumentCaptor.capture());

        assertEquals(notification.getId(), serviceSubscriptionArgumentCaptor.getValue().getId().getNotificationType());
        assertEquals(service, serviceSubscriptionArgumentCaptor.getValue().getId().getService());
        assertEquals(user, serviceSubscriptionArgumentCaptor.getValue().getId().getUser());
        assertTrue(serviceSubscriptionArgumentCaptor.getValue().isSubscribed());
    }

    @Test
    public void positive_subscribe_service_user_is_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Service service = Service.builder().build();
        ServiceSubscription serviceSubscription = ServiceSubscription.builder()
                .id(ServiceSubscription.SubscriptionId.builder().service(service).notificationType("SMS").build())
                .subscribed(true)
                .build();
        User user = User.builder().id(UUID.randomUUID()).serviceSubscriptions(new HashSet<ServiceSubscription>() {{
            add(serviceSubscription);
        }}).build();

        assertFalse(subscriptionService.subscribe(user, service, notification));
        verify(serviceSubscriptionRepository, times(0)).save(any());
    }

    @Test
    public void positive_subscribe_service_user_is_not_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Service service = Service.builder().build();
        ServiceSubscription serviceSubscription = ServiceSubscription.builder()
                .id(ServiceSubscription.SubscriptionId.builder().service(service).notificationType("SMS").build())
                .subscribed(false)
                .build();
        User user = User.builder().id(UUID.randomUUID()).serviceSubscriptions(new HashSet<ServiceSubscription>() {{
            add(serviceSubscription);
        }}).build();

        assertTrue(subscriptionService.subscribe(user, service, notification));
        verify(serviceSubscriptionRepository, times(1)).save(serviceSubscription);
        assertTrue(serviceSubscription.isSubscribed());
    }


    @Test
    public void positive_unsubscribe_monitor_no_subscriptions() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Monitor monitor = mock(Monitor.class);
        User user = User.builder().id(UUID.randomUUID()).monitorSubscriptions(new HashSet<>()).build();

        ArgumentCaptor<MonitorSubscription> monitorSubscriptionArgumentCaptor = ArgumentCaptor.forClass(MonitorSubscription.class);

        assertTrue(subscriptionService.subscribe(user, monitor, notification));
        verify(monitorSubscriptionRepository, times(1)).save(monitorSubscriptionArgumentCaptor.capture());

        assertEquals(notification.getId(), monitorSubscriptionArgumentCaptor.getValue().getId().getNotificationType());
        assertEquals(monitor, monitorSubscriptionArgumentCaptor.getValue().getId().getMonitor());
        assertEquals(user, monitorSubscriptionArgumentCaptor.getValue().getId().getUser());
        assertTrue(monitorSubscriptionArgumentCaptor.getValue().isSubscribed());
    }

    @Test
    public void positive_unsubscribe_monitor_user_is_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Monitor monitor = mock(Monitor.class);
        MonitorSubscription monitorSubscription = MonitorSubscription.builder()
                .id(MonitorSubscription.SubscriptionId.builder().monitor(monitor).notificationType("SMS").build())
                .subscribed(true)
                .build();
        User user = User.builder().monitorSubscriptions(new HashSet<MonitorSubscription>() {{
            add(monitorSubscription);
        }}).build();

        assertTrue(subscriptionService.unsubscribe(user, monitor, notification));
        verify(monitorSubscriptionRepository, times(1)).save(eq(monitorSubscription));
        assertFalse(monitorSubscription.isSubscribed());
    }

    @Test
    public void positive_unsubscribe_monitor_user_is_not_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Monitor monitor = mock(Monitor.class);
        MonitorSubscription monitorSubscription = MonitorSubscription.builder()
                .id(MonitorSubscription.SubscriptionId.builder().monitor(monitor).notificationType("SMS").build())
                .subscribed(false)
                .build();
        User user = User.builder().monitorSubscriptions(new HashSet<MonitorSubscription>() {{
            add(monitorSubscription);
        }}).build();

        assertFalse(subscriptionService.unsubscribe(user, monitor, notification));
        verify(monitorSubscriptionRepository, times(0)).save(any());
    }


    @Test
    public void positive_subscribe_monitor_no_subscriptions() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Monitor monitor = mock(Monitor.class);
        User user = User.builder().id(UUID.randomUUID()).monitorSubscriptions(new HashSet<>()).build();

        ArgumentCaptor<MonitorSubscription> monitorSubscriptionArgumentCaptor = ArgumentCaptor.forClass(MonitorSubscription.class);

        assertTrue(subscriptionService.subscribe(user, monitor, notification));
        verify(monitorSubscriptionRepository, times(1)).save(monitorSubscriptionArgumentCaptor.capture());

        assertEquals(notification.getId(), monitorSubscriptionArgumentCaptor.getValue().getId().getNotificationType());
        assertEquals(monitor, monitorSubscriptionArgumentCaptor.getValue().getId().getMonitor());
        assertEquals(user, monitorSubscriptionArgumentCaptor.getValue().getId().getUser());
        assertTrue(monitorSubscriptionArgumentCaptor.getValue().isSubscribed());
    }

    @Test
    public void positive_subscribe_monitor_user_is_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Monitor monitor = mock(Monitor.class);
        MonitorSubscription monitorSubscription = MonitorSubscription.builder()
                .id(MonitorSubscription.SubscriptionId.builder().monitor(monitor).notificationType("SMS").build())
                .subscribed(true)
                .build();
        User user = User.builder().monitorSubscriptions(new HashSet<MonitorSubscription>() {{
            add(monitorSubscription);
        }}).build();

        assertFalse(subscriptionService.subscribe(user, monitor, notification));
        verify(monitorSubscriptionRepository, times(0)).save(any());
    }

    @Test
    public void positive_subscribe_monitor_user_is_not_subscribed() {
        Notification notification = mock(Notification.class);
        when(notification.getId()).thenReturn("SMS");
        Monitor monitor = mock(Monitor.class);
        MonitorSubscription monitorSubscription = MonitorSubscription.builder()
                .id(MonitorSubscription.SubscriptionId.builder().monitor(monitor).notificationType("SMS").build())
                .subscribed(false)
                .build();
        User user = User.builder().monitorSubscriptions(new HashSet<MonitorSubscription>() {{
            add(monitorSubscription);
        }}).build();

        assertTrue(subscriptionService.subscribe(user, monitor, notification));
        verify(monitorSubscriptionRepository, times(1)).save(monitorSubscription);
        assertTrue(monitorSubscription.isSubscribed());
    }

    @Test
    public void positive_clearSubscriptions_monitor_and_user_typical() {
        Monitor monitor = mock(Monitor.class);
        User user = User.builder().monitorSubscriptions(new HashSet<MonitorSubscription>() {{
            add(MonitorSubscription.builder()
                    .id(MonitorSubscription.SubscriptionId.builder().monitor(monitor).build())
                    .build());
        }}).build();
        assertEquals(1, user.getMonitorSubscriptions().size());

        subscriptionService.clearSubscriptions(user, monitor);
        verify(monitorSubscriptionRepository, times(1)).deleteById_UserAndId_Monitor(eq(user), eq(monitor));
        assertEquals(0, user.getMonitorSubscriptions().size());
    }

    @Test
    public void negative_clearSubscriptions_monitor_and_user_not_subscription_for_monitor() {
        Monitor monitor = mock(Monitor.class);
        User user = User.builder().monitorSubscriptions(new HashSet<>()).build();
        subscriptionService.clearSubscriptions(user, monitor);
        verify(monitorSubscriptionRepository, times(1)).deleteById_UserAndId_Monitor(eq(user), eq(monitor));
        assertEquals(0, user.getMonitorSubscriptions().size());
    }

    @Test
    public void positive_clearSubscriptions_monitor_and_user_multi_monitor() {
        Monitor monitor = mock(Monitor.class);
        MonitorSubscription monitorSubscription = MonitorSubscription.builder()
                .id(MonitorSubscription.SubscriptionId.builder().monitor(monitor).build())
                .build();

        Monitor monitor2 = mock(Monitor.class);
        MonitorSubscription monitor2Subscription = MonitorSubscription.builder()
                .id(MonitorSubscription.SubscriptionId.builder().monitor(monitor2).build())
                .build();

        User user = User.builder().monitorSubscriptions(new HashSet<MonitorSubscription>() {{
            add(monitor2Subscription);
            add(monitorSubscription);
        }}).build();
        assertEquals(2, user.getMonitorSubscriptions().size());

        subscriptionService.clearSubscriptions(user, monitor);
        verify(monitorSubscriptionRepository, times(1)).deleteById_UserAndId_Monitor(eq(user), eq(monitor));
        assertEquals(1, user.getMonitorSubscriptions().size());
        assertTrue(user.getMonitorSubscriptions().contains(monitor2Subscription));
    }

    @Test
    public void positive_clearSubscriptions_monitor_typical() {
        Monitor monitor = mock(Monitor.class);
        subscriptionService.clearSubscriptions(monitor);
        verify(monitorSubscriptionRepository, times(1)).deleteAllById_Monitor(eq(monitor));
    }

    @Test
    public void positive_clearSubscriptions_service_typical() {
        Service service = mock(Service.class);
        subscriptionService.clearSubscriptions(service);
        verify(serviceSubscriptionRepository, times(1)).deleteAllById_Service(eq(service));
    }


}
