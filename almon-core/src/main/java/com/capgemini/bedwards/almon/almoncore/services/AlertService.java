package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.AlertType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public interface AlertService {

    Alert saveAlert(@NotNull AlertType alertType, String message, LocalDateTime timeOfAlert) throws NotFoundException;

    default Alert saveAlert(@NotNull AlertType alertType, String message) throws NotFoundException {
        return saveAlert(alertType, message, LocalDateTime.now());
    }
}
