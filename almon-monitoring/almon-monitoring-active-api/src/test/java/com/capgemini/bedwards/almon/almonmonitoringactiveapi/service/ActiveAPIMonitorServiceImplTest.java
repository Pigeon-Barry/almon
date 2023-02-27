package com.capgemini.bedwards.almon.almonmonitoringactiveapi.service;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitoringTask;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.repositorty.ActiveAPIMonitorRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.MonitorTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorServiceBase;
import com.capgemini.bedwards.almon.almonmonitoringtest.ScheduledMonitorServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                ActiveAPIMonitorServiceImpl.class
        }
)
public class ActiveAPIMonitorServiceImplTest extends ScheduledMonitorServiceBaseTest<ActiveAPIMonitor> {

    @MockBean
    private ActiveAPIMonitorRepository repository;
    @MockBean
    private ActiveAPIAlertService alertService;
    @Autowired
    private ActiveAPIMonitorServiceImpl monitorService;

    @Test
    public void positive_findAll() {
        List<ActiveAPIMonitor> list = new ArrayList<>();
        when(repository.findAll()).thenReturn(list);
        assertEquals(list, monitorService.findAll());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void positive_getRepository() {
        assertEquals(repository, monitorService.getRepository());
    }

    @Test
    public void positive_getScheduledTask() {
        Service service = Service.builder().build();
        ActiveAPIMonitor monitor = getMonitor(service);
        ActiveAPIMonitoringTask result = monitorService.getScheduledTask(monitor);
        assertEquals(monitor.getTaskId(), result.getTASK_ID());
        assertEquals(monitor.getCronExpression(), result.getCronExpression());
        assertEquals(alertService, result.getAlertService());
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

    @Override
    protected Class<ActiveAPIMonitor> getMonitorClass() {
        return ActiveAPIMonitor.class;
    }

    @Override
    protected MonitorServiceBase<ActiveAPIMonitor> getMonitorService() {
        return monitorService;
    }

    @Override
    protected MonitorTypeRepository<ActiveAPIMonitor> getRepository() {
        return repository;
    }
}
