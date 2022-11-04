package com.capgemini.bedwards.almon.almonmonitoringcore.models;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@Slf4j
public class UpdateMonitorRequestBody {
    @NotBlank
    protected String name;
    protected String description;


    protected <T extends Monitor> T toMonitor(T monitor) {
        if (this.getName() != null)
            monitor.setName(this.getName());
        if (this.getDescription() != null)
            monitor.setDescription(this.getDescription());
        return monitor;
    }

    protected UpdateMonitorRequestBody populate(Monitor monitor) {
        this.setName(monitor.getName());
        this.setDescription(monitor.getDescription());
        return this;
    }
}
