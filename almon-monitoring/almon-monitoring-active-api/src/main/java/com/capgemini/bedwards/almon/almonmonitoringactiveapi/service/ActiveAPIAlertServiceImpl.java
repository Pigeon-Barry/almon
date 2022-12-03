package com.capgemini.bedwards.almon.almonmonitoringactiveapi.service;

import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIAlert;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.repositorty.ActiveAPIAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.ScheduledAlertServiceBase;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActiveAPIAlertServiceImpl extends ScheduledAlertServiceBase<ActiveAPIAlert> implements
    ActiveAPIAlertService {


  private final ActiveAPIAlertRepository API_ALERT_REPOSITORY;

  @Autowired
  public ActiveAPIAlertServiceImpl(NotificationService notificationService,
      ActiveAPIAlertRepository apiAlertRepository) {
    super(notificationService);
    this.API_ALERT_REPOSITORY = apiAlertRepository;
  }

    @Override
    protected ActiveAPIAlertRepository getRepository() {
        return API_ALERT_REPOSITORY;
    }
}
