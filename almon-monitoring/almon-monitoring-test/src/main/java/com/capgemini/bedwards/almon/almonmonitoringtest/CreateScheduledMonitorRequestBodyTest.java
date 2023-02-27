package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.CreateScheduledMonitorRequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class CreateScheduledMonitorRequestBodyTest extends CreateMonitorRequestBodyTest {

    protected void populateScheduledMonitorRequestBody(CreateScheduledMonitorRequestBody requestBody) {
        populateMonitorRequestBody(requestBody);
        requestBody.setCronExpression("* * * * * *");
    }

    protected void validateScheduledMonitorRequestBody(CreateScheduledMonitorRequestBody requestBody, ScheduledMonitor monitor, Service service) {
        validateMonitorRequestBody(requestBody, monitor, service);
        assertEquals(requestBody.getCronExpression(), monitor.getCronExpression());
    }
}
