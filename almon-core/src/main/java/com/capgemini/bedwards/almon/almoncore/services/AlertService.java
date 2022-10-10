package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almoncore.models.Alert;
import com.capgemini.bedwards.almon.almoncore.models.AlertType;

import java.time.LocalDateTime;

public interface AlertService {

    Alert saveAlert(AlertType alertType, String message, LocalDateTime timeOfAlert);

    default Alert saveAlert(AlertType alertType, String message) {
        return saveAlert(alertType, message, LocalDateTime.now());
    }
}
