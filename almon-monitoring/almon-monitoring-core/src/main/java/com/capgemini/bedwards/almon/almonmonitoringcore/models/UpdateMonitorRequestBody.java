package com.capgemini.bedwards.almon.almonmonitoringcore.models;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMonitorRequestBody {
    @NotBlank
    protected String name;


    protected String description;

    @Min(0)
    @NotNull
    protected Long notificationThrottle;

    protected <T extends Monitor> T toMonitor(T monitor) {
        if (this.getName() != null)
            monitor.setName(this.getName());
        if (this.getDescription() != null)
            monitor.setDescription(this.getDescription());
        if (this.getNotificationThrottle() != null)
            monitor.setNotificationThrottle(this.getNotificationThrottle());
        return monitor;
    }

    protected UpdateMonitorRequestBody populate(Monitor monitor) {
        this.setName(monitor.getName());
        this.setDescription(monitor.getDescription());
        this.setNotificationThrottle(monitor.getNotificationThrottle());
        return this;
    }
}
