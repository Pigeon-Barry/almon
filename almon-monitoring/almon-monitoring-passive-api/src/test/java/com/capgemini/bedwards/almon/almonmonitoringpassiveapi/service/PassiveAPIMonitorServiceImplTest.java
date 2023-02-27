package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.service;


import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.MonitorTypeRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorServiceBase;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.repository.PassiveAPIMonitorRepository;
import com.capgemini.bedwards.almon.almonmonitoringtest.MonitorServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                PassiveAPIMonitorServiceImpl.class
        }
)
public class PassiveAPIMonitorServiceImplTest extends MonitorServiceBaseTest<PassiveAPIMonitor> {
    @MockBean
    private PassiveAPIMonitorRepository repository;
    @Autowired
    private PassiveAPIMonitorServiceImpl monitorService;

    @Test
    public void positive_findAll() {
        List<PassiveAPIMonitor> list = new ArrayList<>();
        when(repository.findAll()).thenReturn(list);
        assertEquals(list, monitorService.findAll());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void positive_getRepository() {
        assertEquals(repository, monitorService.getRepository());
    }

    @Override
    protected Class<PassiveAPIMonitor> getMonitorClass() {
        return PassiveAPIMonitor.class;
    }

    @Override
    protected MonitorServiceBase<PassiveAPIMonitor> getMonitorService() {
        return monitorService;
    }

    @Override
    protected MonitorTypeRepository<PassiveAPIMonitor> getRepository() {
        return repository;
    }


}
