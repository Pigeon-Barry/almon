package com.capgemini.bedwards.almon.almondatastore.models.schedule;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
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
public abstract class ScheduledMonitoringType extends MonitoringType {

    private String cronExpression;

    public abstract String getTaskId();


    public abstract ScheduledTask getScheduledTask();
}
