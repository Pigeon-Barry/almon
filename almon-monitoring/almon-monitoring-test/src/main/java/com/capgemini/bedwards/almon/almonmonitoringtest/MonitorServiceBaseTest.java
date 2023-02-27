package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almoncore.exceptions.BadRequestException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.subscription.SubscriptionService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.MonitorTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorServiceBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

public abstract class MonitorServiceBaseTest<T extends Monitor> {

    @MockBean
    protected AuthorityService authorityService;
    @MockBean
    protected SubscriptionService subscriptionService;
    @MockBean
    protected ServiceService serviceService;


    protected abstract Class<T> getMonitorClass();

    protected abstract MonitorServiceBase<T> getMonitorService();

    protected abstract MonitorTypeRepository<T> getRepository();

    @Test
    public void positive_enable() {
        T monitor = mock(getMonitorClass());
        getMonitorService().enable(monitor);
        verify(monitor, times(1)).setEnabled(eq(true));
        verify(getRepository(), times(1)).save(eq(monitor));
    }

    @Test
    public void positive_disable() {
        T monitor = mock(getMonitorClass());
        getMonitorService().disable(monitor);
        verify(monitor, times(1)).setEnabled(eq(false));
        verify(getRepository(), times(1)).save(eq(monitor));
    }

    @Test
    public void positive_delete() {
        T monitor = mock(getMonitorClass());
        getMonitorService().delete(monitor);
        verify(authorityService, times(1)).deleteMonitorAuthorities(eq(monitor));
        verify(subscriptionService, times(1)).clearSubscriptions(eq(monitor));
        verify(getRepository(), times(1)).delete(eq(monitor));
    }


    public static Stream<Arguments> createIdProvider() {
        return Stream.of(
                arguments("SERVICE_ID", "MON_ID"),
                arguments("NEW_SERV", "NEW_MON"),
                arguments("THIS_IS", "WILD")
        );
    }

    @ParameterizedTest
    @MethodSource("createIdProvider")
    public void positive_create(String serviceId, String monitorId) {
        Service service = Service.builder().id(serviceId).build();
        T monitor = mock(getMonitorClass());
        when(monitor.getId()).thenReturn(Monitor.MonitorId.builder().service(service).id(monitorId).build());

        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(serviceService.getOrCreateUserRole(eq(service))).thenReturn(standardRole);
        when(serviceService.getOrCreateAdminRole(eq(service))).thenReturn(adminRole);


        Set<Authority> adminAuth = new HashSet<Authority>() {{
            add(Authority.builder()
                    .authority("SERVICE_" + service.getId() + "_MONITOR_" + monitor.getId() + "_CAN_ENABLE_DISABLE")
                    .description("Grants the ability to enable/disable this monitor")
                    .build());
            add(Authority.builder()
                    .authority("SERVICE_" + service.getId() + "_MONITOR_" + monitor.getId() + "_CAN_DELETE")
                    .description("Grants the ability to Delete this monitor")
                    .build());
            add(Authority.builder()
                    .authority("SERVICE_" + service.getId() + "_MONITOR_" + monitor.getId() + "_CAN_UPDATE")
                    .description("Grants the ability to update  this monitor")
                    .build());
        }};
        Set<Authority> standardAuth = new HashSet<Authority>() {{
            add(Authority.builder()
                    .authority("SERVICE_" + service.getId() + "_MONITOR_" + monitor.getId() + "_CAN_VIEW")
                    .description("Grants the ability to view this monitor")
                    .build());
            add(Authority.builder()
                    .authority("SERVICE_" + service.getId() + "_MONITOR_" + monitor.getId() + "_CAN_RUN")
                    .description("Grants the ability to run this monitor")
                    .build());
        }};

        adminAuth.forEach(authority -> when(authorityService.createAuthority(eq(authority.getAuthority()), eq(authority.getDescription()))).thenReturn(authority));
        standardAuth.forEach(authority -> when(authorityService.createAuthority(eq(authority.getAuthority()), eq(authority.getDescription()))).thenReturn(authority));

        when(getRepository().save(eq(monitor))).thenReturn(monitor);

        assertEquals(monitor, getMonitorService().create(monitor));

        verify(getRepository(), times(1)).save(eq(monitor));
        verify(monitor, times(1)).setPreventNotificationUntil(eq(Constants.DEFAULT_MONITOR_PREVENT_UNTIL));

        adminAuth.forEach(authority -> verify(authorityService, times(1)).addRole(eq(authority), eq(Collections.singleton(adminRole))));
        standardAuth.forEach(authority -> verify(authorityService, times(1)).addRole(eq(authority), eq(Collections.singleton(standardRole))));

        verify(authorityService, times(1)).refreshRole(eq(standardRole));
        verify(authorityService, times(1)).refreshRole(eq(adminRole));
    }

    @ParameterizedTest
    @MethodSource("createIdProvider")
    public void positive_getMonitorFromCombinedId(String serviceId, String monitorId) {
        T monitor = mock(getMonitorClass());
        Optional<T> optional = Optional.of(monitor);
        Monitor.MonitorId monitorIdObj = Monitor.MonitorId.builder()
                .id(monitorId)
                .service(com.capgemini.bedwards.almon.almondatastore.models.service.Service.builder()
                        .id(serviceId).build())
                .build();
        when(getRepository().findById(eq(monitorIdObj))).thenReturn(optional);

        assertEquals(monitor, getMonitorService().getMonitorFromCombinedId(serviceId + "-" + monitorId));
        verify(getRepository(), times(1)).findById(eq(monitorIdObj));
    }

    @Test
    public void negative_getMonitorFromCombinedId_invalid_number_of_segments() {
        assertThrows(BadRequestException.class, () -> getMonitorService().getMonitorFromCombinedId("MON_ID_SERVICE_ID"), "Could not determine service and monitor id from 'MON_ID_SERVICE-ID'");
    }

    @Test
    public void negative_getMonitorFromCombinedId_not_found() {
        Optional<T> optional = Optional.empty();
        Monitor.MonitorId monitorIdObj = Monitor.MonitorId.builder()
                .id("MON_ID")
                .service(com.capgemini.bedwards.almon.almondatastore.models.service.Service.builder()
                        .id("SERVICE_ID").build())
                .build();
        when(getRepository().findById(eq(monitorIdObj))).thenReturn(optional);
        assertThrows(NotFoundException.class, () -> getMonitorService().getMonitorFromCombinedId("MON_ID-SERVICE_ID"), "Failed to find monitor with id: 'MON_ID-SERVICE_ID'");
    }
}
