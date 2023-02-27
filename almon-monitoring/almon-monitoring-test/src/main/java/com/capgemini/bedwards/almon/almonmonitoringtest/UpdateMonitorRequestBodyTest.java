package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.UpdateMonitorRequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class UpdateMonitorRequestBodyTest {

    protected void validatePopulateMonitorRequestBody(UpdateMonitorRequestBody requestBody, Monitor monitor) {
        assertEquals(monitor.getName(), requestBody.getName());
        assertEquals(monitor.getDescription(), requestBody.getDescription());
        assertEquals(monitor.getNotificationThrottle(), requestBody.getNotificationThrottle());
    }

    protected void populateMonitorRequestBody(UpdateMonitorRequestBody requestBody) {
        requestBody.setName("NAME");
        requestBody.setDescription("DESC");
        requestBody.setNotificationThrottle(5000L);
    }

    protected void validateMonitorRequestBody(UpdateMonitorRequestBody requestBody, Monitor newMonitor, Monitor oldMonitor, Service service) {
        validateUpdate(requestBody.getName(), newMonitor.getName(), oldMonitor.getName());
        validateUpdate(requestBody.getDescription(), newMonitor.getDescription(), oldMonitor.getDescription());
        validateUpdate(requestBody.getNotificationThrottle(), newMonitor.getNotificationThrottle(), oldMonitor.getNotificationThrottle());
    }

    protected <T> void validateUpdate(T requestBodyValue, T newValue, T oldValue) {
        if (requestBodyValue != null)
            assertEquals(requestBodyValue, newValue);
        else
            assertEquals(oldValue, newValue);
    }
}
