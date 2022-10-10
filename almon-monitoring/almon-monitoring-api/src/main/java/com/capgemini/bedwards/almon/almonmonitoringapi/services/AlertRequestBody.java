package com.capgemini.bedwards.almon.almonmonitoringapi.services;

import com.capgemini.bedwards.almon.almoncore.models.AlertType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AlertRequestBody {
    private AlertType alertType;
    private String message;
    private LocalDateTime time;
}
