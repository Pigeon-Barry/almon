package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.UpdateScheduledMonitorRequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class UpdateScheduledMonitorRequestBodyTest extends UpdateMonitorRequestBodyTest {

    protected void validatePopulateScheduledMonitorRequestBody(UpdateScheduledMonitorRequestBody requestBody, ScheduledMonitor monitor) {
        validatePopulateMonitorRequestBody(requestBody, monitor);
        assertEquals(monitor.getCronExpression(), requestBody.getCronExpression());
    }

    protected void populateScheduledMonitorRequestBody(UpdateScheduledMonitorRequestBody requestBody) {
        populateMonitorRequestBody(requestBody);
        requestBody.setCronExpression("* * * * * *");
    }

    protected void validateScheduledMonitorRequestBody(UpdateScheduledMonitorRequestBody requestBody, ScheduledMonitor newMonitor, ScheduledMonitor oldMonitor, Service service) {
        validateMonitorRequestBody(requestBody, newMonitor, oldMonitor, service);
        validateUpdate(requestBody.getCronExpression(), newMonitor.getCronExpression(), oldMonitor.getCronExpression());
    }

}
