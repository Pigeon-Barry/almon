package com.capgemini.bedwards.almon.almondatastore.models.schedule;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
public abstract class ScheduledMonitor extends Monitor {

    private String cronExpression;

    public String getTaskId() {
        return getMonitorType() + "-" + getId().toString();
    }

    public abstract ScheduledTask getScheduledTask();
}
