package com.capgemini.bedwards.almon.almonalertingcore;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.alerts.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alerts.AlertType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AlertService {

    Alert saveAlert(@NotNull UUID alertTypeId, String message, LocalDateTime timeOfAlert) throws NotFoundException;

    default Alert saveAlert(@NotNull UUID alertTypeId, String message) throws NotFoundException {
        return saveAlert(alertTypeId, message, LocalDateTime.now());
    }
}
