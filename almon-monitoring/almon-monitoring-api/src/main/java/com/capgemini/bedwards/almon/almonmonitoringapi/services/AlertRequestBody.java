package com.capgemini.bedwards.almon.almonmonitoringapi.services;

import com.capgemini.bedwards.almon.almondatastore.models.AlertType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class AlertRequestBody {
    @NotNull
    private AlertType alertType;
    private String message;
    @Schema(description = "The time the alert was triggered (Default: is the request was received)")
    private LocalDateTime time;
}
