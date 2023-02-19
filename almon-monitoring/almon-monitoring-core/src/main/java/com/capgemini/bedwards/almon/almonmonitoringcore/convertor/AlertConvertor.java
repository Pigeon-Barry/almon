package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class AlertConvertor implements Converter<UUID, Alert<?>> {


    public final AlertService<Alert<?>> ALERT_SERVICE;

    public AlertConvertor(@Qualifier("alertServiceImpl") AlertService<Alert<?>> alertService) {
        this.ALERT_SERVICE = alertService;
    }

    @Override
    public Alert<?> convert(UUID source) {
        return ALERT_SERVICE.getAlertFromId(source);
    }
}
