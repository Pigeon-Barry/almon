package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.AlertType;
import com.capgemini.bedwards.almon.almondatastore.repository.AlertRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.AlertTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AlertServiceImpl implements AlertService {


    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private AlertTypeRepository alertTypeRepository;

    @Override
    public Alert saveAlert(@NotNull AlertType alertType, String message, LocalDateTime timeOfAlert) throws NotFoundException {
        if (timeOfAlert == null)
            timeOfAlert = LocalDateTime.now();
        if (!alertTypeRepository.existsById(alertType.getName())) {
            throw new NotFoundException("Alert type: '" + alertType.getName() + "' does not exist");
        }

        Alert alert = Alert.builder()
                .alertType(alertType)
                .message(message)
                .alertReceivedTime(timeOfAlert)
                .build();
        log.debug("Alert received: " + alert);
        return alertRepository.save(alert);
    }
}
