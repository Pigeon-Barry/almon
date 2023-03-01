package com.capgemini.bedwards.almon.almonaccessapi.controller.alert;


import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestAlert;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestMonitor;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AlertApiController.class)
@ContextConfiguration(
        classes = {
                AlertApiController.class
        }
)
public class AlertApiControllerTest extends APIControllerTest {

    private final String CONTROLLER_BASEURL = "/api/service/{serviceId}/monitor/{monitorId}/alert/{alertId}";
    private final String CONTROLLER_BASEURL_HTML = CONTROLLER_BASEURL + "/html";
    private final String CONTROLLER_BASEURL_JSON = CONTROLLER_BASEURL + "/json";
    private final String RESOURCE_PREFIX = "/testData/alertAPIController";


    public Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        return Stream.of(
                StandardTestsArguments.of(
                        "/html",
                        () -> {
                            Service service = setupValidService("myServiceId");
                            TestMonitor monitor = setupValidMonitor(service, "myMonitorId");
                            TestAlert testAlert = setupValidAlert(monitor, UUID.fromString("67e99747-db96-4c52-881e-7a43b6624153"));
                        },
                        get(CONTROLLER_BASEURL_HTML, "myServiceId", "myServiceId-myMonitorId", "67e99747-db96-4c52-881e-7a43b6624153"),
                        Collections.singletonList("VIEW_ALL_MONITORS")
                ),
                StandardTestsArguments.of(
                        "/json",
                        () -> {
                            Service service = setupValidService("myServiceId");
                            TestMonitor monitor = setupValidMonitor(service, "myMonitorId");
                            TestAlert testAlert = setupValidAlert(monitor, UUID.fromString("67e99747-db96-4c52-881e-7a43b6624153"));
                        },
                        get(CONTROLLER_BASEURL_HTML, "myServiceId", "myServiceId-myMonitorId", "67e99747-db96-4c52-881e-7a43b6624153"),
                        Collections.singletonList("VIEW_ALL_MONITORS")
                )
        );
    }


    @Test
    public void positive_viewAlertDetailsAsHtml() throws Exception {
        addAuthority("VIEW_ALL_MONITORS");
        Service service = setupValidService("myServiceId");
        TestMonitor monitor = setupValidMonitor(service, "myMonitorId");
        TestAlert testAlert = setupValidAlert(monitor, UUID.fromString("67e99747-db96-4c52-881e-7a43b6624153"));
        this.mockMvc
                .perform(
                        get(CONTROLLER_BASEURL_HTML, "myServiceId", "myServiceId-myMonitorId", "67e99747-db96-4c52-881e-7a43b6624153")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().exists("CorrelationId"))
                .andExpect(content().xml(readResource("ALERT_FOUND.html", RESOURCE_PREFIX)));

    }

    @Test
    public void positive_viewAlertDetailsAsJson() throws Exception {
        addAuthority("VIEW_ALL_MONITORS");
        Service service = setupValidService("myServiceId");
        TestMonitor monitor = setupValidMonitor(service, "myMonitorId");
        TestAlert testAlert = setupValidAlert(monitor, UUID.fromString("67e99747-db96-4c52-881e-7a43b6624153"));
        this.mockMvc
                .perform(
                        get(CONTROLLER_BASEURL_JSON, "myServiceId", "myServiceId-myMonitorId", "67e99747-db96-4c52-881e-7a43b6624153")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().exists("CorrelationId"))
                .andExpect(content().json(readResource("ALERT_FOUND.json", RESOURCE_PREFIX), true));

    }


    @ParameterizedTest
    @ValueSource(strings = {CONTROLLER_BASEURL_HTML, CONTROLLER_BASEURL_JSON})
    public void negative_viewAlertDetailsAs_serviceNotFound(String url) throws Exception {
        addAuthority("VIEW_ALL_MONITORS");
        when(serviceConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Service with Key: '" + invocation.getArgument(0) + "' not found");
        });
        this.mockMvc
                .perform(
                        get(url, "NOTFOUND", "NOTFOUND", UUID.randomUUID())
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(header().exists("CorrelationId"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(content().json(readResource("SERVICE_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }


    @ParameterizedTest
    @ValueSource(strings = {CONTROLLER_BASEURL_HTML, CONTROLLER_BASEURL_JSON})
    public void negative_viewAlertDetailsAs_monitorNotFound(String url) throws Exception {
        addAuthority("VIEW_ALL_MONITORS");
        setupValidService("myServiceId");
        when(monitorConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Failed to find monitor with id: '" + invocation.getArgument(0) + "'");
        });
        this.mockMvc
                .perform(
                        get(url, "myServiceId", "NOT-FOUND", UUID.randomUUID())
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(header().exists("CorrelationId"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(content().json(readResource("MONITOR_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }

    @ParameterizedTest
    @ValueSource(strings = {CONTROLLER_BASEURL_HTML, CONTROLLER_BASEURL_JSON})
    public void negative_viewAlertDetailsAs_alertNotFound(String url) throws Exception {
        addAuthority("VIEW_ALL_MONITORS");
        Service service = setupValidService("myServiceId");
        setupValidMonitor(service, "myMonitorId");
        when(alertStringConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Failed to find alert with id: '" + invocation.getArgument(0) + "'");
        });
        this.mockMvc
                .perform(
                        get(url, "myServiceId", "myServiceId-myMonitorId", "67e99747-db96-4c52-881e-7a43b6624153")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().exists("CorrelationId"))
                .andExpect(content().json(readResource("ALERT_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }
}
