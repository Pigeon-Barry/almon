package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringtest.CreateScheduledMonitorRequestBodyTest;
import com.capgemini.bedwards.almon.notificationcore.NotificationCoreConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class CreateActiveAPIMonitorRequestBodyTest extends CreateScheduledMonitorRequestBodyTest {

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
        CreateActiveAPIMonitorRequestBody requestBody = CreateActiveAPIMonitorRequestBody.builder()
                .method(method)
                .url(url)
                .body(body)
                .expectedStatusCode(expectedStatusCode)
                .maxResponseTime(maxResponseTime)
                .headers(headers)
                .jsonPathValidations(jsonPathValidations)
                .build();
        populateScheduledMonitorRequestBody(requestBody);
        Service service = Service.builder().build();

        ActiveAPIMonitor monitor = requestBody.toAPIMonitor(service);

        validateScheduledMonitorRequestBody(requestBody, monitor, service);
        assertEquals(requestBody.getUrl(), monitor.getUrl());
        assertEquals(requestBody.getMethod(), monitor.getMethod());
        assertEquals(requestBody.getBody(), monitor.getBody());
        assertEquals(requestBody.getHeaders(), monitor.getHeaders());
        assertEquals(requestBody.getJsonPathValidations(), monitor.getJsonPathValidations());
        assertEquals(requestBody.getExpectedStatusCode(), monitor.getExpectedStatus());
        assertEquals(requestBody.getMaxResponseTime(), monitor.getMaxResponseTime());

    }


}
