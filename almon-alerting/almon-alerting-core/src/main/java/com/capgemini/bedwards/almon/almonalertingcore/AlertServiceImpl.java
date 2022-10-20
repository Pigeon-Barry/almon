package com.capgemini.bedwards.almon.almonalertingcore;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.alerts.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alerts.AlertType;
import com.capgemini.bedwards.almon.almondatastore.repository.AlertRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.AlertTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AlertServiceImpl implements AlertService {


    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private AlertTypeRepository alertTypeRepository;

    @Override
    public Alert saveAlert(@NotNull UUID alertTypeId, String message, LocalDateTime timeOfAlert) throws NotFoundException {
        if (timeOfAlert == null)
            timeOfAlert = LocalDateTime.now();
        Optional<AlertType> alertTypeOptional = alertTypeRepository.findById(alertTypeId);
        if (!alertTypeOptional.isPresent()) {
            throw new NotFoundException("Alert type: '" + alertTypeId + "' does not exist");
        }


        Alert alert = Alert.builder()
                .alertType(alertTypeOptional.get())
                .message(message)
                .alertReceivedTime(timeOfAlert)
                .build();
        log.debug("Alert received: " + alert);
        return alertRepository.save(alert);
    }
}
