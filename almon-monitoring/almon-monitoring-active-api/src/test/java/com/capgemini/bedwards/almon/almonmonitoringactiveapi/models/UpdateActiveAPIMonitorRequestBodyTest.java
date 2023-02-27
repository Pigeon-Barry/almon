package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringtest.UpdateScheduledMonitorRequestBodyTest;
import com.capgemini.bedwards.almon.notificationcore.NotificationCoreConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UpdateActiveAPIMonitorRequestBodyTest extends UpdateScheduledMonitorRequestBodyTest {

    @MockBean
    private ApplicationContext applicationContext;

    @BeforeEach
    public void beforeEach() {
        BeanUtil.setApplicationContext(applicationContext);
        when(applicationContext.getBean(eq(NotificationCoreConfig.class))).thenReturn(new NotificationCoreConfig());
    }

    public static Stream<Arguments> toAPIMonitorProvider() {
        return Stream.of(
                arguments(HttpMethod.GET, "www.almon.com", "{\"data\":true}", 200, 500, new HashMap<String, String>() {{
                    put("key", "value");
                    put("api-key", "myApiKey");
                }}, new HashMap<String, String>() {{
                    put("myJson.path", "value");
                    put("$.myOther.json.path", "val2");
                }}),
                arguments(HttpMethod.GET, null, null, null, null, null, null),
                arguments(null, "www.almon.com", null, null, null, null, null),
                arguments(null, null, "{\"data\":true}", null, null, null, null),
                arguments(null, null, null, 200, null, null, null),
                arguments(null, null, null, null, 500, new HashMap<String, String>() {{
                    put("key", "value");
                    put("api-key", "myApiKey");
                }}, null),
                arguments(null, null, null, null, null, null, new HashMap<String, String>() {{
                    put("myJson.path", "value");
                    put("$.myOther.json.path", "val2");
                }})
        );
    }

    @ParameterizedTest
    @MethodSource("toAPIMonitorProvider")
    public void positive_toAPIMonitor(HttpMethod method, String url, String body, Integer expectedStatusCode, Integer maxResponseTime, Map<String, String> headers, Map<String, String> jsonPathValidations) {
        Service service = Service.builder().build();
        ActiveAPIMonitor oldMonitor = getMonitor(service);
        ActiveAPIMonitor newMonitor = getMonitor(service);

        UpdateActiveAPIMonitorRequestBody requestBody = UpdateActiveAPIMonitorRequestBody.builder()
                .method(method)
                .url(url)
                .body(body)
                .expectedStatusCode(expectedStatusCode)
                .maxResponseTime(maxResponseTime)
                .headers(headers)
                .jsonPathValidations(jsonPathValidations)
                .build();
        populateScheduledMonitorRequestBody(requestBody);

        newMonitor = requestBody.updateAPIMonitor(newMonitor);

        validateScheduledMonitorRequestBody(requestBody, newMonitor, oldMonitor, service);


        validateUpdate(requestBody.getUrl(), newMonitor.getUrl(), oldMonitor.getUrl());
        validateUpdate(requestBody.getMethod(), newMonitor.getMethod(), oldMonitor.getMethod());
        validateUpdate(requestBody.getBody(), newMonitor.getBody(), oldMonitor.getBody());
        validateUpdate(requestBody.getHeaders(), newMonitor.getHeaders(), oldMonitor.getHeaders());
        validateUpdate(requestBody.getJsonPathValidations(), newMonitor.getJsonPathValidations(), oldMonitor.getJsonPathValidations());
        validateUpdate(requestBody.getExpectedStatusCode(), newMonitor.getExpectedStatus(), oldMonitor.getExpectedStatus());
        validateUpdate(requestBody.getMaxResponseTime(), newMonitor.getMaxResponseTime(), oldMonitor.getMaxResponseTime());

    }


    @Test
    public void positive_populate() {
        Service service = Service.builder().build();
        ActiveAPIMonitor monitor = getMonitor(service);
        UpdateActiveAPIMonitorRequestBody requestBody = new UpdateActiveAPIMonitorRequestBody();
        requestBody.populate(monitor);

        validatePopulateScheduledMonitorRequestBody(requestBody, monitor);
        assertEquals(monitor.getMethod(), requestBody.getMethod());
        assertEquals(monitor.getUrl(), requestBody.getUrl());
        assertEquals(monitor.getBody(), requestBody.getBody());
        assertEquals(monitor.getExpectedStatus(), requestBody.getExpectedStatusCode());
        assertEquals(monitor.getHeaders(), requestBody.getHeaders());
        assertEquals(monitor.getJsonPathValidations(), requestBody.getJsonPathValidations());
        assertEquals(monitor.getMaxResponseTime(), requestBody.getMaxResponseTime());

    }

    protected ActiveAPIMonitor getMonitor(Service service) {
        return ActiveAPIMonitor.builder()
                .method(HttpMethod.TRACE)
                .url("www.old.url.com")
                .expectedStatus(500)
                .maxResponseTime(1)
                .body("BODY")
                .cronExpression("* * * * * *")
                .id(Monitor.MonitorId.builder().service(service).id("KEY").build())
                .name("MON")
                .description("MON_DESC")
                .enabled(true)
                .notificationThrottle(1L)
                .preventNotificationUntil(LocalDateTime.now())
                .alerts(new HashSet<>())
                .subscriptions(new HashSet<>())
                .headers(new HashMap<>())
                .jsonPathValidations(new HashMap<>())
                .build();
    }
}
