package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.service;

import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIAlert;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.repository.PassiveAPIAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringtest.AlertServiceBaseTest;
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
                PassiveAPIAlertServiceImpl.class
        }
)
public class PassiveAPIAlertServiceImplTest extends AlertServiceBaseTest<PassiveAPIAlert> {
    @MockBean
    private PassiveAPIAlertRepository repository;

    @Autowired
    private PassiveAPIAlertServiceImpl alertService;

    @Test
    public void positive_getRepository() {
        assertEquals(repository, alertService.getRepository());
    }

    @Override
    protected Class<PassiveAPIAlert> getAlertClass() {
        return PassiveAPIAlert.class;
    }

    @Override
    protected PassiveAPIAlertRepository getRepository() {
        return repository;
    }

    @Override
    protected PassiveAPIAlertServiceImpl getAlertService() {
        return alertService;
    }

}
