package com.capgemini.bedwards.almon.almonaccessapi.controller.monitor;

import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestMonitor;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestScheduledMonitor;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.ScheduledMonitorAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MonitorAPIController.class)
@ContextConfiguration(
        classes = {
                MonitorAPIController.class
        }
)
public class MonitorAPIControllerTest extends APIControllerTest {

    private static final String CONTROLLER_BASEURL = "/api/service/{serviceId}/monitor/{monitorId}";

    private static final String CONTROLLER_BASEURL_ENABLE = CONTROLLER_BASEURL + "/enable";
    private static final String CONTROLLER_BASEURL_DISABLE = CONTROLLER_BASEURL + "/disable";
    private static final String CONTROLLER_BASEURL_RUN = CONTROLLER_BASEURL + "/run";
    private static final String CONTROLLER_BASEURL_UPDATE = CONTROLLER_BASEURL + "/update";
    private static final String RESOURCE_PREFIX = "/testData/monitorAPIController";

    @MockBean
    private Monitors monitors;

    @MockBean
    private ScheduledMonitorAdapter<TestScheduledMonitor, ?> scheduledMonitorAdapter;
    @MockBean
    private MonitorAdapter<TestMonitor, ?> monitorAdapter;

    @Override
    protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        Runnable setup = () -> {
            Service service = setupValidService("myServiceId");
            setupValidMonitor(service, "myMonitorId");
            setupValidScheduledMonitor(service, "myMonitorId");
        };

        return Stream.of(
                StandardTestsArguments.of(
                        "Get Monitor",
                        setup,
                        get(CONTROLLER_BASEURL, "myServiceId", "myServiceId-myMonitorId"),
                        Collections.singletonList("VIEW_ALL_MONITORS")
                ), StandardTestsArguments.of(
                        "Enable",
                        setup,
                        put(CONTROLLER_BASEURL_ENABLE, "myServiceId", "myServiceId-myMonitorId"),
                        Collections.singletonList("ENABLE_DISABLE_MONITORS")
                ), StandardTestsArguments.of(
                        "Disable",
                        setup,
                        put(CONTROLLER_BASEURL_DISABLE, "myServiceId", "myServiceId-myMonitorId"),
                        Collections.singletonList("ENABLE_DISABLE_MONITORS")
                ), StandardTestsArguments.of(
                        "Delete",
                        setup,
                        delete(CONTROLLER_BASEURL, "myServiceId", "myServiceId-myMonitorId"),
                        Collections.singletonList("DELETE_MONITORS")
                ), StandardTestsArguments.of(
                        "Run",
                        setup,
                        post(CONTROLLER_BASEURL_RUN, "myServiceId", "myServiceId-myMonitorId"),
                        Collections.singletonList("RUN_MONITORS")
                ), StandardTestsArguments.of(
                        "Update",
                        setup,
                        put(CONTROLLER_BASEURL_UPDATE, "myServiceId", "myServiceId-myMonitorId"),
                        Collections.singletonList("UPDATE_MONITORS")
                )


        );
    }

    private static Stream<Arguments> requestBuilderProvider() {
        return Stream.of(
                Arguments.of(get(CONTROLLER_BASEURL, "myServiceId", "myServiceId-myMonitorId")),
                Arguments.of(put(CONTROLLER_BASEURL_ENABLE, "myServiceId", "myServiceId-myMonitorId")),
                Arguments.of(put(CONTROLLER_BASEURL_DISABLE, "myServiceId", "myServiceId-myMonitorId")),
                Arguments.of(delete(CONTROLLER_BASEURL, "myServiceId", "myServiceId-myMonitorId")),
                Arguments.of(post(CONTROLLER_BASEURL_RUN, "myServiceId", "myServiceId-myMonitorId")),
                Arguments.of(put(CONTROLLER_BASEURL_UPDATE, "myServiceId", "myServiceId-myMonitorId"))
        );
    }

    @ParameterizedTest
    @MethodSource("requestBuilderProvider")
    public void negative_service_not_found(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        addAuthorities("VIEW_ALL_MONITORS", "ENABLE_DISABLE_MONITORS", "DELETE_MONITORS", "RUN_MONITORS", "UPDATE_MONITORS");
        when(serviceConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Service with Key: '" + invocation.getArgument(0) + "' not found");
        });

        setupValidMonitor(Service.builder().id("myServiceId").build(), "myMonitorId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        requestBuilder
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("SERVICE_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }

    @ParameterizedTest
    @MethodSource("requestBuilderProvider")
    public void negative_monitor_not_found(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        addAuthorities("VIEW_ALL_MONITORS", "ENABLE_DISABLE_MONITORS", "DELETE_MONITORS", "RUN_MONITORS", "UPDATE_MONITORS");
        setupValidService("myServiceId");
        when(monitorConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Failed to find monitor with id: '" + invocation.getArgument(0) + "'");
        });
        when(scheduledMonitorConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Failed to find monitor with id: '" + invocation.getArgument(0) + "'");
        });
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        requestBuilder
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("MONITOR_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }


    @Test
    public void positive_get_monitor() throws Exception {
        addAuthorities("VIEW_ALL_MONITORS");
        Service service = setupValidService("myServiceId");
        setupValidMonitor(service, "myMonitorId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(get(CONTROLLER_BASEURL, "myServiceId", "myServiceId-myMonitorId")
                        .header("x-api-key", TEST_AUTH_TOKEN)
                        .header("CorrelationId", correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("GET_MONITOR.json", RESOURCE_PREFIX), true));
    }

    @Test
    public void positive_enable_monitor() throws Exception {
        addAuthorities("ENABLE_DISABLE_MONITORS");
        Service service = setupValidService("myServiceId");
        TestMonitor monitor = setupValidMonitor(service, "myMonitorId");

        doReturn(monitorServiceImpl).when(monitors).getMonitorServiceFromMonitor(monitor);

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(put(CONTROLLER_BASEURL_ENABLE, "myServiceId", "myServiceId-myMonitorId")
                        .header("x-api-key", TEST_AUTH_TOKEN)
                        .header("CorrelationId", correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(monitorServiceImpl, times(1)).enable(monitor);
    }

    @Test
    public void positive_disable_monitor() throws Exception {
        addAuthorities("ENABLE_DISABLE_MONITORS");
        Service service = setupValidService("myServiceId");
        TestMonitor monitor = setupValidMonitor(service, "myMonitorId");

        doReturn(monitorServiceImpl).when(monitors).getMonitorServiceFromMonitor(monitor);

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(put(CONTROLLER_BASEURL_DISABLE, "myServiceId", "myServiceId-myMonitorId")
                        .header("x-api-key", TEST_AUTH_TOKEN)
                        .header("CorrelationId", correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(monitorServiceImpl, times(1)).disable(monitor);
    }

    @Test
    public void positive_delete_monitor() throws Exception {
        addAuthorities("DELETE_MONITORS");
        Service service = setupValidService("myServiceId");
        TestMonitor monitor = setupValidMonitor(service, "myMonitorId");

        doReturn(monitorServiceImpl).when(monitors).getMonitorServiceFromMonitor(monitor);

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(delete(CONTROLLER_BASEURL, "myServiceId", "myServiceId-myMonitorId")
                        .header("x-api-key", TEST_AUTH_TOKEN)
                        .header("CorrelationId", correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(monitorServiceImpl, times(1)).delete(monitor);
    }

    @Test
    public void positive_run_monitor() throws Exception {
        addAuthorities("RUN_MONITORS");
        Service service = setupValidService("myServiceId");
        TestScheduledMonitor monitor = setupValidScheduledMonitor(service, "myMonitorId");

        doReturn(scheduledMonitorAdapter).when(monitors).getMonitorAdapterFromMonitor(monitor);

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(post(CONTROLLER_BASEURL_RUN, "myServiceId", "myServiceId-myMonitorId")
                        .header("x-api-key", TEST_AUTH_TOKEN)
                        .header("CorrelationId", correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(scheduledMonitorAdapter, times(1)).execute(monitor);
    }

    @Test
    public void positive_update_monitor() throws Exception {
        addAuthorities("UPDATE_MONITORS");
        Service service = setupValidService("myServiceId");
        TestMonitor monitor = setupValidMonitor(service, "myMonitorId");

        doReturn(monitorAdapter).when(monitors).getMonitorAdapterFromMonitor(monitor);

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(put(CONTROLLER_BASEURL_UPDATE, "myServiceId", "myServiceId-myMonitorId")
                        .header("x-api-key", TEST_AUTH_TOKEN)
                        .header("CorrelationId", correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(monitorAdapter, times(1)).updateMonitor(eq(monitor), any(), any());
    }


}
