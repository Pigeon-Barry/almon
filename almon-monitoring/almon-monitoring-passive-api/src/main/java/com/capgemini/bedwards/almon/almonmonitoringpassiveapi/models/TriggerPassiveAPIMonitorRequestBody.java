package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TriggerPassiveAPIMonitorRequestBody {

    @NotNull
    @NotBlank
    private String message;
    @NotNull
    private Status status;

    public PassiveAPIAlert toAlert(PassiveAPIMonitor monitor) {
        return PassiveAPIAlert.builder()
                .monitor(monitor)
                .status(this.getStatus())
                .message(this.getMessage())
                .build();
    }
}
