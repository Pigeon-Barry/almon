package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConvertor implements Converter<String, Notification> {


    public final NotificationService NOTIFICATION_SERVICE;

    public NotificationConvertor(NotificationService notificationService) {
        this.NOTIFICATION_SERVICE = notificationService;
    }

    @Override
    public Notification convert(String source) {
        return NOTIFICATION_SERVICE.getNotificationFromId(source);
    }
}