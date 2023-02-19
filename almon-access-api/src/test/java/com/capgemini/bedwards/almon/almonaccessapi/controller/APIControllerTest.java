package com.capgemini.bedwards.almon.almonaccessapi.controller;

import com.capgemini.bedwards.almon.almonaccessapi.models.TestAlert;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestMonitor;
import com.capgemini.bedwards.almon.almonaccessapi.models.TestScheduledMonitor;
import com.capgemini.bedwards.almon.almonaccessapi.util.APIWebConfig;
import com.capgemini.bedwards.almon.almoncore.security.AlmonAuthenticationProvider;
import com.capgemini.bedwards.almon.almoncore.security.SecurityConfig;
import com.capgemini.bedwards.almon.almoncore.services.APIKeyService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.convertor.*;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertServiceImpl;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorServiceImpl;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(
        classes = {
                MonitorConvertor.class,
                AlertConvertor.class,
                AlertStringConvertor.class,
                ServiceConvertor.class,
                APIWebConfig.class,
                SecurityConfig.class
        }
)
@AutoConfigureMockMvc
public abstract class APIControllerTest {

    private final String RESOURCE_PREFIX = "/testData/general";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected MonitorConvertor monitorConvertor;

    @MockBean
    protected ScheduledMonitorConvertor scheduledMonitorConvertor;

    @MockBean
    protected AlertConvertor alertConvertor;
    @MockBean
    protected AlertStringConvertor alertStringConvertor;
    @MockBean
    protected ServiceConvertor serviceConvertor;

    @MockBean(name = "monitorServiceImpl")
    protected MonitorServiceImpl monitorServiceImpl;

    @MockBean(name = "scheduledMonitorServiceImpl")
    protected ScheduledMonitorServiceImpl scheduledMonitorServiceImpl;

    @MockBean(name = "alertServiceImpl")
    protected AlertServiceImpl alertServiceImpl;

    @MockBean
    protected ServiceService serviceService;

    @MockBean
    protected AlmonAuthenticationProvider almonAuthenticationProvider;
    @MockBean
    protected APIKeyService apiKeyService;

    protected final String TEST_AUTH_TOKEN = "TEST_TOKEN_" + UUID.randomUUID();
    private final List<String> TEST_AUTHORITIES = new ArrayList<>();

    @BeforeEach
    public void before() {
        this.TEST_AUTHORITIES.clear();
        updateAuthorities();
    }

    protected void addAuthorities(String... authorities) {
        addAuthorities(Arrays.asList(authorities));
    }

    protected void addAuthorities(List<String> authorities) {
        this.TEST_AUTHORITIES.addAll(authorities);
        updateAuthorities();
    }

    protected void addAuthority(String authority) {
        this.TEST_AUTHORITIES.add(authority);
        updateAuthorities();

    }

    private void updateAuthorities() {
        reset(this.apiKeyService);
        when(this.apiKeyService.getAPIKey(eq(this.TEST_AUTH_TOKEN))).thenReturn(
                getTestAPIKey()
        );
    }

    private APIKey getTestAPIKey() {
        APIKey.APIKeyBuilder apiKeyBuilder = APIKey.builder()
                .apiKey(this.TEST_AUTH_TOKEN)
                .enabled(true);
        Set<Authority> authorities = new HashSet<>();
        for (String authority : this.TEST_AUTHORITIES) {
            authorities.add(
                    Authority.builder()
                            .authority(authority)
                            .build()
            );
        }

        apiKeyBuilder.authorities(authorities);
        return apiKeyBuilder.build();
    }


    protected Service setupValidService(String id) {
        Service service = Service.builder()
                .id(id)
                .name("Service Name")
                .description("Service Description")
                .enabled(true)
                .build();
        return setupValidService(service);
    }

    protected Service setupValidService(Service service) {
        when(serviceConvertor.convert(eq(service.getId()))).thenReturn(service);
        when(serviceService.findServiceById(eq(service.getId()))).thenReturn(service);
        return service;
    }

    protected TestMonitor setupValidMonitor(Service service, String id) {
        TestMonitor monitor = TestMonitor.builder()
                .id(Monitor.MonitorId.builder()
                        .service(service)
                        .id(id)
                        .build())
                .name("Monitor Name")
                .description("Monitor Description")
                .enabled(true)
                .build();
        return setupValidMonitor(monitor);
    }

    protected TestScheduledMonitor setupValidScheduledMonitor(TestScheduledMonitor monitor) {
        when(scheduledMonitorConvertor.convert(eq(monitor.getId().toString()))).thenReturn(monitor);
        when(scheduledMonitorServiceImpl.getMonitorFromCombinedId(eq(monitor.getId().toString()))).thenReturn(monitor);
        return monitor;
    }

    protected TestScheduledMonitor setupValidScheduledMonitor(Service service, String id) {
        TestScheduledMonitor monitor = TestScheduledMonitor.builder()
                .id(Monitor.MonitorId.builder()
                        .service(service)
                        .id(id)
                        .build())
                .name("Monitor Name")
                .description("Monitor Description")
                .enabled(true)
                .build();
        return setupValidScheduledMonitor(monitor);
    }

    protected TestMonitor setupValidMonitor(TestMonitor monitor) {
        when(monitorConvertor.convert(eq(monitor.getId().toString()))).thenReturn(monitor);
        when(monitorServiceImpl.getMonitorFromCombinedId(eq(monitor.getId().toString()))).thenReturn(monitor);
        return monitor;
    }

    protected TestAlert setupValidAlert(TestMonitor monitor, UUID id) {
        TestAlert alert = TestAlert.builder()
                .id(id)
                .createdAt(Instant.ofEpochMilli(1676771477341L).atZone(ZoneId.systemDefault()).toLocalDateTime())
                .monitor(monitor)
                .status(Status.PASS)
                .message("Test Alert Message")
                .build();
        return setupValidAlert(alert);
    }

    protected TestAlert setupValidAlert(TestAlert alert) {
        doReturn(alert).when(alertConvertor).convert(alert.getId());
        doReturn(alert).when(alertStringConvertor).convert(alert.getId().toString());
        doReturn(alert).when(alertServiceImpl).getAlertFromId(alert.getId());
        return alert;
    }

    protected abstract Stream<StandardTestsArguments> getStandardTestsArgumentProvider();


    @ParameterizedTest(name = "Invalid CorrelationId: {0}")
    @MethodSource("getStandardTestsArgumentProvider")
    void negative_invalidCorrelationId(String name, Runnable preTest, MockHttpServletRequestBuilder requestBuilder, List<String> authorities) throws Exception {
        addAuthorities(authorities);
        preTest.run();
        this.mockMvc
                .perform(
                        requestBuilder
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("CorrelationId", "INVALID")
                )
                .andDo(print())
                .andExpect(header().doesNotExist("CorrelationId"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().json(readResource("INVALID_CORRELATIONID.json", RESOURCE_PREFIX), true));
    }

    @ParameterizedTest(name = "Unauthorized: {0}")
    @MethodSource("getStandardTestsArgumentProvider")
    void negative_unauthorized(String name, Runnable preTest, MockHttpServletRequestBuilder requestBuilder, List<String> authorities) throws Exception {
        preTest.run();
        this.mockMvc
                .perform(
                        requestBuilder
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("CorrelationId", UUID.randomUUID())
                )
                .andDo(print())
                .andExpect(header().exists("CorrelationId"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(content().json(readResource("UNAUTHORISED_API.json", RESOURCE_PREFIX), true));
    }


    protected interface StandardTestsArguments extends Arguments {
        static StandardTestsArguments of(String name, Runnable preTest, MockHttpServletRequestBuilder requestBuilder, List<String> authorities) {
            return () -> new Object[]{name, preTest, requestBuilder, authorities};
        }
    }
}
