package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.ScheduledAlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.ScheduledAlertServiceBase;

public abstract class ScheduledAlertServiceBaseTest<T extends ScheduledAlert<?>> extends AlertServiceBaseTest<T> {

    protected abstract ScheduledAlertRepository<T> getRepository();

    protected abstract ScheduledAlertServiceBase<T> getAlertService();
}
