package com.capgemini.bedwards.almon.almonapi.models.alerts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class AlertRequestBody {
    @NotNull
    private UUID alertTypeId;
    private String message;
    @Schema(description = "The time the alert was triggered (Default: is the request was received)")
    private LocalDateTime time;
}
