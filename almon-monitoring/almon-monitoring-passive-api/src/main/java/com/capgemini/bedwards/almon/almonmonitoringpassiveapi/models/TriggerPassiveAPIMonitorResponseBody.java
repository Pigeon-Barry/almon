package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TriggerPassiveAPIMonitorResponseBody {

    private UUID id;
    private String monitor;
    private LocalDateTime createdAt;
    private Status status;
    private String message;

    public TriggerPassiveAPIMonitorResponseBody(PassiveAPIAlert alert) {
        this.setId(alert.getId());
        this.setMonitor(alert.getMonitor().getId().toString());
        this.setCreatedAt(alert.getCreatedAt());
        this.setStatus(alert.getStatus());
        this.setMessage(alert.getMessage());
    }
}
