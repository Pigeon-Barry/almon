package com.capgemini.bedwards.almon.almonaccessapi.controller.monitor;


import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestAlert;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestMonitor;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.CreateMonitorRequestResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ServiceMonitorApiController.class)
@ContextConfiguration(
        classes = {
                ServiceMonitorApiController.class,
                CreateMonitorRequestResolver.class
        }
)
public class ServiceMonitorApiControllerTest extends APIControllerTest {

    private static final String CONTROLLER_BASEURL = "/api/service/{serviceId}/monitor/{monitorId}";

    private static final String CONTROLLER_BASEURL_CREATE = CONTROLLER_BASEURL + "/create";
    private static final String RESOURCE_PREFIX = "/testData/serviceMonitorApiController";

    @MockBean
    private Monitors monitors;
    @MockBean
    private MonitorAdapter<TestMonitor, TestAlert> monitorAdapter;


    @Override
    protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        return Stream.of(
                StandardTestsArguments.of(
                        "Create Monitor",
                        () -> {
                            Service service = setupValidService("myServiceId");
                            setupValidMonitor(service, "myMonitorId");
                            when(monitorAdapterConvertor.convert(any())).thenAnswer(invocation -> monitorAdapter);
                        },
                        post(CONTROLLER_BASEURL_CREATE, "myServiceId", "TESTADAPATER")
                                .content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX)),
                        Collections.singletonList("CREATE_MONITORS")
                ));
    }

    @Test
    public void positive_create_service() throws Exception {
        addAuthorities("CREATE_MONITORS");
        Service service = setupValidService("myServiceId");
        setupValidMonitor(service, "myMonitorId");
        when(monitorAdapterConvertor.convert(any())).thenAnswer(invocation -> monitorAdapter);

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        post(CONTROLLER_BASEURL_CREATE, "myServiceId", "TESTADAPATER")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX))
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(monitorAdapter, times(1)).createMonitor(any(), any(), any());
    }

    @Test
    public void negative_service_not_found() throws Exception {
        addAuthorities("CREATE_MONITORS");
        when(serviceConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Service with Key: '" + invocation.getArgument(0) + "' not found");
        });

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        post(CONTROLLER_BASEURL_CREATE, "myServiceId", "TESTADAPATER")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("SERVICE_NOT_FOUND.json", RESOURCE_PREFIX), true));
        verify(monitorAdapter, never()).createMonitor(any(), any(), any());
    }

    @Test
    public void negative_adapter_not_found() throws Exception {
        addAuthorities("CREATE_MONITORS");
        setupValidService("myServiceId");
        when(monitorAdapterConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Service with Key: '" + invocation.getArgument(0) + "' not found");
        });
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        post(CONTROLLER_BASEURL_CREATE, "myServiceId", "TESTADAPATER")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("MONITOR_ADAPTER_NOT_FOUND.json", RESOURCE_PREFIX), true));
        verify(monitorAdapter, never()).createMonitor(any(), any(), any());
    }

}
