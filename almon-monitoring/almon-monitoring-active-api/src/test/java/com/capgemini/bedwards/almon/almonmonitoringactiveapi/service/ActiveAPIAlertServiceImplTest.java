package com.capgemini.bedwards.almon.almonmonitoringactiveapi.service;

import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIAlert;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.repositorty.ActiveAPIAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringtest.ScheduledAlertServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                ActiveAPIAlertServiceImpl.class
        }
)
public class ActiveAPIAlertServiceImplTest extends ScheduledAlertServiceBaseTest<ActiveAPIAlert> {

    @MockBean
    private ActiveAPIAlertRepository repository;

    @Autowired
    private ActiveAPIAlertServiceImpl alertService;

    @Test
    public void positive_getRepository() {
        assertEquals(repository, alertService.getRepository());
    }

    @Override
    protected Class<ActiveAPIAlert> getAlertClass() {
        return ActiveAPIAlert.class;
    }

    @Override
    protected ActiveAPIAlertRepository getRepository() {
        return repository;
    }

    @Override
    protected ActiveAPIAlertServiceImpl getAlertService() {
        return alertService;
    }
}
