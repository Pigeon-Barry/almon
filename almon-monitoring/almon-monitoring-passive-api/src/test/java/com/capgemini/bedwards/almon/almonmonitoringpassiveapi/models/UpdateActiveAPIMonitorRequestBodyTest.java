package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringtest.UpdateMonitorRequestBodyTest;
import com.capgemini.bedwards.almon.notificationcore.NotificationCoreConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UpdateActiveAPIMonitorRequestBodyTest extends UpdateMonitorRequestBodyTest {

    @MockBean
    private ApplicationContext applicationContext;

    @BeforeEach
    public void beforeEach() {
        BeanUtil.setApplicationContext(applicationContext);
        when(applicationContext.getBean(eq(NotificationCoreConfig.class))).thenReturn(new NotificationCoreConfig());
    }


    public static Stream<Arguments> toAPIMonitorProvider() {
        return Stream.of(
                arguments(null, null, null),
                arguments("NEW_NAME", null, null),
                arguments(null, "NEW_DESC", null),
                arguments(null, null, 10000L),
                arguments("NEW_NAME2", "NEW_DESC2", 10000L)
        );
    }

    @ParameterizedTest
    @MethodSource("toAPIMonitorProvider")
    public void positive_toAPIMonitor(String name, String description, Long notificationThrottle) {
        Service service = Service.builder().build();
        PassiveAPIMonitor oldMonitor = getMonitor(service);
        PassiveAPIMonitor newMonitor = getMonitor(service);

        UpdatePassiveAPIMonitorRequestBody requestBody = new UpdatePassiveAPIMonitorRequestBody(name, description, notificationThrottle);
        populateMonitorRequestBody(requestBody);

        newMonitor = requestBody.updateAPIMonitor(newMonitor);

        validateMonitorRequestBody(requestBody, newMonitor, oldMonitor, service);
    }


    @Test
    public void positive_populate() {
        Service service = Service.builder().build();
        PassiveAPIMonitor monitor = getMonitor(service);
        UpdatePassiveAPIMonitorRequestBody requestBody = new UpdatePassiveAPIMonitorRequestBody();
        requestBody.populate(monitor);
        validatePopulateMonitorRequestBody(requestBody, monitor);
    }

    protected PassiveAPIMonitor getMonitor(Service service) {
        return PassiveAPIMonitor.builder()
                .id(Monitor.MonitorId.builder().service(service).id("KEY").build())
                .name("MON")
                .description("MON_DESC")
                .enabled(true)
                .notificationThrottle(1L)
                .preventNotificationUntil(LocalDateTime.now())
                .alerts(new HashSet<>())
                .subscriptions(new HashSet<>())
                .build();
    }
}
