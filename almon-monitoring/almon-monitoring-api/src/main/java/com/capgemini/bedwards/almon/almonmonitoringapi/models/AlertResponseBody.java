package com.capgemini.bedwards.almon.almonmonitoringapi.models;


import com.capgemini.bedwards.almon.almondatastore.models.alerts.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AlertResponseBody {
    UUID id;

    public static AlertResponseBody from(Alert saveAlert) {
        return new AlertResponseBody(saveAlert.getId());
    }
}