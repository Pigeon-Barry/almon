package com.capgemini.bedwards.almon.almonmonitoringcore.service.alert;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.ScheduledAlertRepository;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class ScheduledAlertServiceImpl<T extends ScheduledAlert>
        extends AlertServiceImpl<T>
        implements ScheduledAlertService<T> {


    @Autowired
    public ScheduledAlertServiceImpl(NotificationService notificationService) {
        super(notificationService);
    }

    protected abstract ScheduledAlertRepository<T> getRepository();

}
