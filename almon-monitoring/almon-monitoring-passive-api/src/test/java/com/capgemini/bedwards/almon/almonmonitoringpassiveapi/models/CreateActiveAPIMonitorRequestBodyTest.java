package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringtest.CreateMonitorRequestBodyTest;
import com.capgemini.bedwards.almon.notificationcore.NotificationCoreConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class CreateActiveAPIMonitorRequestBodyTest extends CreateMonitorRequestBodyTest {

    @MockBean
    private ApplicationContext applicationContext;

    @BeforeEach
    public void beforeEach() {
        BeanUtil.setApplicationContext(applicationContext);
        when(applicationContext.getBean(eq(NotificationCoreConfig.class))).thenReturn(new NotificationCoreConfig());
    }

    @Test
    public void positive_toAPIMonitor() {
        CreatePassiveAPIMonitorRequestBody requestBody = CreatePassiveAPIMonitorRequestBody.builder().build();
        populateMonitorRequestBody(requestBody);
        Service service = Service.builder().build();

        PassiveAPIMonitor monitor = requestBody.toAPIMonitor(service);

        validateMonitorRequestBody(requestBody, monitor, service);

    }


}
