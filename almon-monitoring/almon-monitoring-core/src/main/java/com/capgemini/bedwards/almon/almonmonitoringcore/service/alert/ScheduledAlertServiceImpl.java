package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.ScheduledAlertRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ScheduledAlertServiceImpl<T extends ScheduledAlert>
        extends AlertServiceImpl<T>
        implements ScheduledAlertService<T> {


    protected abstract ScheduledAlertRepository<T> getRepository();

}
