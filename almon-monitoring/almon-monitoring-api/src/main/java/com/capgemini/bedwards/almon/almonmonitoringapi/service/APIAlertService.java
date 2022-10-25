package com.capgemini.bedwards.almon.almonmonitoringapi.service;

import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIAlertType;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.ScheduledAlertService;


public interface APIAlertService extends ScheduledAlertService<APIAlertType> {
}
