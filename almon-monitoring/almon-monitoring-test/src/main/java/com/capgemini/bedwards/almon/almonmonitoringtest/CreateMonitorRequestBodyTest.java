package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.CreateMonitorRequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class CreateMonitorRequestBodyTest {
    protected void populateMonitorRequestBody(CreateMonitorRequestBody requestBody) {
        requestBody.setKey("KEY");
        requestBody.setName("NAME");
        requestBody.setDescription("DESC");
        requestBody.setNotificationThrottle(5000L);
    }

    protected void validateMonitorRequestBody(CreateMonitorRequestBody requestBody, Monitor monitor, Service service) {
        assertEquals(service, monitor.getId().getService());
        assertEquals(requestBody.getKey(), monitor.getId().getId());
        assertEquals(requestBody.getName(), monitor.getName());
        assertEquals(requestBody.getDescription(), monitor.getDescription());
        assertEquals(requestBody.getNotificationThrottle(), monitor.getNotificationThrottle());
    }
}
